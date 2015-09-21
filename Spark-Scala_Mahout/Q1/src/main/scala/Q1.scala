import java.util.Random
import scala.math
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
object Q1 {

	def Classification(p: (Double,Double), random:Array[(Double,Double)]): Int = {
    
	var index = 0
    var clusId = 0
    var currDist = Double.PositiveInfinity

		for (i <- 0 to 2) 
		{
			var tempCtr = random(i)
			var tempDist = math.sqrt(math.pow(p._1.toDouble - tempCtr._1.toDouble,2)+ math.pow(p._2.toDouble - tempCtr._2.toDouble,2))
			if (tempDist < currDist) 
			{
				currDist = tempDist
				clusId = i
			}
		}
		
		clusId
	}

	def main(args: Array[String]) 
	{
		val testFile = "src/data/Q1_testkmean.txt" 
		val sc = new SparkContext("local", "K-Means App", "/path/to/spark-0.9.1-incubating",
		List("target/scala-2.10/simple-project_2.10-1.0.jar"))
		
		val cacheData = sc.textFile(testFile, 1).cache()
		val points = sc.textFile(testFile, 1).map(line=>line.split(" ")).map(line=>(line(0).toDouble,line(1).toDouble))
		
		
		var random =  points.takeSample(false, 3, 1987)

		
		var iteration = 0
		while(iteration <=10)
		{
			var classifiedPoints = points.map(p=> (Classification(p,random), (p,1.0)))
			
			var clusterCenters = classifiedPoints.reduceByKey((x,y)=>(((x._1._1 + y._1._1),(x._1._2 + y._1._2)),(x._2 + y._2)))
			
			var newCentroid = clusterCenters.mapValues{ case (point, sum) => (point._1/sum,point._2/sum)}
			
			
			if( iteration == 10 ){
				
				newCentroid.saveAsTextFile("src/data/newCentroid")
				var ClusterAssociation = classifiedPoints.mapValues{ case (point,count) => (point._1,point._2)}
				ClusterAssociation.saveAsTextFile("src/data/ClusterAssociation")
			}
			
			random = newCentroid.values.toArray
			iteration = iteration + 1
			
		}
	}
}

