package com.bigdata.DocumentClasification

import java.io.{File, FilenameFilter}

import com.gravity.goose.{Configuration, Goose}
import jline.ConsoleReader
import org.apache.spark.SparkContext
import org.apache.log4j.Logger
import org.apache.log4j.Level
//for naive bayes 
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
//for logistic regression 
import org.apache.spark.mllib.classification.{LogisticRegressionWithLBFGS, LogisticRegressionModel,LogisticRegressionWithSGD}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
//for gradient boosted trees
import org.apache.spark.mllib.tree.GradientBoostedTrees
import org.apache.spark.mllib.tree.configuration.BoostingStrategy
//for random decision trees
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.tree.configuration.Strategy
object NaiveBayesExample extends App {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  // 4 workers
  val sc = new SparkContext("local[4]", "naivebayes")

  val dataDir = if (args.length == 1) {
    args.head
  } else {
    "reuters"
  }

  val naiveBayesAndDictionaries = createNaiveBayesModel(dataDir)
  console(naiveBayesAndDictionaries)

  /**
   * REPL loop to enter different urls
   */
  def console(naiveBayesAndDictionaries: NaiveBayesAndDictionaries) = {
    println("Enter 'q' to quit")
    val consoleReader = new ConsoleReader()
    while ( {
      consoleReader.readLine("exit> ") match {
        case s if s == "q" => false
        case url: String =>
          predict(naiveBayesAndDictionaries, url)
          true
        case _ => true
      }
    }) {}

    sc.stop()
  }

  def predict(naiveBayesAndDictionaries: NaiveBayesAndDictionaries, url: String) = {
    // extract content from web page
    val config = new Configuration
    config.setEnableImageFetching(false)
    val goose = new Goose(config)
    val content = goose.extractContent(url).cleanedArticleText
    // tokenize content and stem it
    val tokens = Tokenizer.tokenize(content)
    // compute TFIDF vector
    val tfIdfs = naiveBayesAndDictionaries.termDictionary.tfIdfs(tokens, naiveBayesAndDictionaries.idfs)
    val vector = naiveBayesAndDictionaries.termDictionary.vectorize(tfIdfs)
    val labelId = naiveBayesAndDictionaries.model.predict(vector)

    // convert label from double
    println("Label: " + naiveBayesAndDictionaries.labelDictionary.valueOf(labelId.toInt))
  }

  /**
   *
   * @param directory
   * @return
   */
  def createNaiveBayesModel(directory: String) = {
    val inputFiles = new File(directory).list(new FilenameFilter {
      override def accept(dir: File, name: String) = name.endsWith(".sgm")
    })

    val fullFileNames = inputFiles.map(directory + "/" + _)
	//println(fullFileNames[1])
    val docs = ReutersParser.parseAll(fullFileNames)
	
	//println(docs)
    val termDocs = Tokenizer.tokenizeAll(docs)

    // put collection in Spark
    val termDocsRdd = sc.parallelize[TermDoc](termDocs.toSeq)

    val numDocs = termDocs.size
	println("Number of Documents :-------------------------------------------------------------------> " + numDocs.toString)
	

    // create dictionary term => id
    // and id => term
    val terms = termDocsRdd.flatMap(_.terms).distinct().collect().sortBy(identity)
    val termDict = new Dictionary(terms)

    val labels = termDocsRdd.flatMap(_.labels).distinct().collect()
    val labelDict = new Dictionary(labels)

    // compute TFIDF and generate vectors
    // for IDF
    val idfs = (termDocsRdd.flatMap(termDoc => termDoc.terms.map((termDoc.doc, _))).distinct().groupBy(_._2) collect {
      // if term is present in less than 3 documents then remove it
      case (term, docs) if docs.size > 3 =>
        term -> (numDocs.toDouble / docs.size.toDouble)
    }).collect.toMap

    val tfidfs = termDocsRdd flatMap {
      termDoc =>
        val termPairs = termDict.tfIdfs(termDoc.terms, idfs)
        // we consider here that a document only belongs to the first label
        termDoc.labels.headOption.map {
          label =>
            val labelId = labelDict.indexOf(label).toDouble
            val vector = Vectors.sparse(termDict.count, termPairs)
            LabeledPoint(labelId, vector)
        }
    }
	
	
	
	val splits = tfidfs.randomSplit(Array(0.8, 0.2), seed = 11L)
	println("Splitting the data into 80 - 20 for training and testing respectively")
	val training = splits(0).cache()
	val test = splits(1)
	

	
	// Run Naive Bayes training algorithm to build the model
    val model = NaiveBayes.train(training,lambda = 1.0 )
	val predictionAndLabel = test.map(p => (model.predict(p.features), p.label))
	val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()
	
		
	
	// Run logistic regression training algorithm to build the model
	val lr_model = new LogisticRegressionWithLBFGS().setNumClasses(24).run(training)	
	val predictionAndLabels = test.map(p => (lr_model.predict(p.features), p.label))
	val lr_accuracy = 1.0 * predictionAndLabels.filter(x => x._1 == x._2).count() / test.count()
	
	// Run a GradientBoostedTrees model
	val boostingStrategy = BoostingStrategy.defaultParams("Classification")
	boostingStrategy.numIterations = 7 // Note: Use more in practice
	val gbt_model = GradientBoostedTrees.train(training, boostingStrategy)
    val GBT_pred = test.map(p => (gbt_model.predict(p.features), p.label))
	val gbt_accuracy = 1.0 * GBT_pred.filter(x => x._1 == x._2).count() / test.count()
	
	println("Accuracy of GBT :" + gbt_accuracy)

	
	val numClasses = 24
	val categoricalFeaturesInfo = Map[Int, Int]()
	val numTrees = 10 // Use more in practice.
	val featureSubsetStrategy = "auto" // Let the algorithm choose.
	val impurity = "gini"
	val maxDepth = 9
	val maxBins = 32
	val rf_model = RandomForest.trainClassifier(training, numClasses, categoricalFeaturesInfo,numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins)
	val rf_pred = test.map(p => (rf_model.predict(p.features), p.label))
	val rf_accuracy = 1.0 * rf_pred.filter(x => x._1 == x._2).count() / test.count()
	println(" ")
	println("Accuracies on test data")
	println("__________________________________________ ")
	println("Naive Bayes Accuracy")
	println(accuracy)
	println("Logistic Classifier Accuracy")
	println(lr_accuracy)
	println("Accuracy of Random Forests  :" )
	println(rf_accuracy)
	println("Accuracy of GBT :")
	println(gbt_accuracy)

  
	println("Done with building a model")
    NaiveBayesAndDictionaries(model, termDict, labelDict, idfs)
	
  }
}

case class NaiveBayesAndDictionaries(model: NaiveBayesModel,
                                     termDictionary: Dictionary,
                                     labelDictionary: Dictionary,
                                     idfs: Map[String, Double])
