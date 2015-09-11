Assignment 2
Shubham Rai
scr130130


This assignments was developed in Ecilpse IDE (java 1.7 sdk) on Windows 8.1 platform using java as a programming language.

Instructions for running the program:

users folder has users.dat file
movie folder has movie.dat file
rating folder has ratings.dat file

Part 1: 
Type:
hadoop jar rateMovie.jar rateMovie  rating hw21output 3268

To see the output:
hdfs dfs -cat hw21output/*


Part 2: 
Type:
hadoop jar avgRatedMovie.jar avgRatedMovie rating users movie hw22Output

To see the output:
hdfs dfs -cat hw22output/*
