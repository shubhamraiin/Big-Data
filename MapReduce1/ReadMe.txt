Assignment 1
Shubham Rai
scr130130


This assignments was developed in Ecilpse IDE (java 1.7 sdk) on Windows 8.1 platform using java as a programming language.

Instructions for running the program:

Create two input folders:hw1input and movie and put the users.dat in hw1input directory and movies.dat in movie directory.

hdfs dfs -mkdir hw1input
hdfs dfs -put users.dat hw1input

hdfs dfs -mkdir movie
hdfs dfs -put movies.dat movie

Part 1: List all male user id whose age is less or equal to 7
Type:
hadoop jar listUser.jar listUser hw1input output

To see the output:
hdfs dfs -cat output/*


Part 2: Find the count of female and male users in each age group
Type:
hadoop jar countUser.jar countUser hw1input countOutput

To see the output:
hdfs dfs -cat countOutput/*


Part 3: List all movie title where genre is Fantasy

Type:
hadoop jar movieList.jar listMovie movie movieOutput Fantasy
(make sure the its "Fantasy" and not "fantasy")

To see the output:
hdfs dfs -cat movieOutput/*


