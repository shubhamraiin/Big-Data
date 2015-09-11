movies = LOAD '/usr/lib/hue/inputHw3/movies.dat' using PigStorage(':') AS (MOVIEID:chararray, TITLE:chararray, GENRES:chararray);

users = LOAD '/usr/lib/hue/inputHw3/users.dat' using PigStorage(':') AS (USERID:chararray, GENDER:chararray, AGE:int, OCCUPATION:chararray, ZIPCODE:chararray);

ratings = LOAD '/usr/lib/hue/inputHw3/ratings.dat' using PigStorage(':') AS (USERID:chararray, MOVIEID:chararray, RATING:int, TIMESTAMP:chararray);

A = FILTER movies BY (GENRES matches '.*Comedy.*') AND (GENRES matches '.*Drama.*');

B = FOREACH A Generate MOVIEID;

C = GROUP ratings BY MOVIEID;

D = FOREACH C GENERATE  group,AVG(ratings.RATING) AS D;

E = JOIN B BY $0, D BY $0;

F = ORDER E by $2 ASC;

G = FOREACH F GENERATE $0 AS MOVIEID, $2 AS AvgRating;

H = LIMIT G 1;

I = FOREACH H Generate $0 AS MOVIEID; 

J = JOIN ratings BY MOVIEID, I BY MOVIEID;

K = FILTER users BY (GENDER == 'M') AND (AGE < 40) AND (AGE > 20) AND ZIPCODE matches '1.*';

L = JOIN K BY USERID, J BY ratings::USERID;

M = FOREACH L GENERATE $0 AS USERID;

N = DISTINCT M;

DUMP N;