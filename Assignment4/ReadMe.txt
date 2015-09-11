Assignment 4
scr130130


Q1
Using spark-0.9.1 version

Inside Q1 folder There is simple.sbt file, src folder, src/main/scala/Q1.scala, and src/data/Q1_tetskmean.txt


Go to spark-0.9.1\bin\Q1
To compile

Type: sbt package

To run:

Type: sbt run

Output file are generated in src/data
there will be two folders that will be created: ClusterAssociation and newCenroid.

Each of those folders have part-00000 files which contain the respective outputs:
• Print  each point and the corresponding cluster it belongs to.
• Print the final centroids

Q2:

There are two files Q2_1.txt and Q2_2.txt
To execute:

1. spark-shell -i Q2_2.scala
2. To see the Output : Reco.main(null)

OR
1.Open Q2_1.txt and Copy the command and paste in on the cs6360 cluster
2.run spark-shell
3.Open Q2_2.txt and copy the entire program and paste in on the terminal and press Enter

To see the ouput
Type: Reco.main(null)
 

I have provide the output in: Q2_output.txt for Userid- 20
