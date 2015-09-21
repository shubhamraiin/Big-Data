movies = LOAD '/usr/lib/hue/inputHw3/movies.dat' using PigStorage(':') AS (MOVIEID:chararray, TITLE:chararray, GENRES:chararray);

ratings = LOAD '/usr/lib/hue/inputHw3/ratings.dat' using PigStorage(':') AS (USERID:chararray, MOVIEID:chararray, RATING:int, TIMESTAMP:chararray);

A = COGROUP movies BY MOVIEID,ratings by MOVIEID;

B = Limit A 6;

DESCRIBE B;

DUMP B;