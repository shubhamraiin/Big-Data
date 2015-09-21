movies = LOAD '/usr/lib/hue/inputHw3/movies.dat' using PigStorage(':') AS (MOVIEID:chararray, TITLE:chararray, GENRES:chararray);

ratings = LOAD '/usr/lib/hue/inputHw3/ratings.dat' using PigStorage(':') AS (USERID:chararray, MOVIEID:chararray, RATING:int, TIMESTAMP:chararray);

A = COGROUP movies BY MOVIEID inner,ratings by MOVIEID inner;

B =  FOREACH A GENERATE FLATTEN(movies),FLATTEN(ratings);

C = Limit B 6;

DESCRIBE C;

DUMP C; 