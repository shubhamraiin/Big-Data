Project 1
Shubham Rai
scr130130


This assignments was developed in Ecilpse IDE (java 1.7 sdk) on Windows 10 platform using java as a programming language  and Hortonworks Sandbox with HDP 2.2

Instructions for running the program:

Create input folders:"data" movie and put all the .csv files in  directory.

hdfs dfs -mkdir data
hdfs dfs -put data/*.csv data

I have provided a jar file for the program

Need to pass 5 arguments to the program:
hadoop jar CrimeAnalysis.jar CrimeAnalysis args1 args2 args3 args4 args5
arg1= input folder i.e data
arg2= output folder
arg3= Region Definition i.e 1 or 3
arg4= number of mappers
arg5= numer of reducers


To run
type:
hadoop jar CrimeAnalysis.jar CrimeAnalysis data output 1 2 2


To see the output:
hdfs dfs -cat output/*

