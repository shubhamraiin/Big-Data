REGISTER /usr/lib/hue/pig/pig_udf.jar
 
movies = LOAD '/usr/lib/hue/inputHw3/movies.dat' using PigStorage(':') AS (MOVIEID:chararray, TITLE:chararray, GENRES:chararray);
 
B = FOREACH movies GENERATE TITLE,FORMAT_GENRE(GENRES);
 
DUMP B;

--STORE B INTO '/usr/lib/hue/pig/output/';