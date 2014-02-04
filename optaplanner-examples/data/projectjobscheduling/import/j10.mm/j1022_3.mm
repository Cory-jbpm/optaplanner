************************************************************************
file with basedata            : mm22_.bas
initial value random generator: 1886678714
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  12
horizon                       :  90
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     10      0       20        5       20
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          2           8  11
   3        3          3           5   6   8
   4        3          1           6
   5        3          2           7  10
   6        3          1           7
   7        3          1           9
   8        3          2           9  10
   9        3          1          12
  10        3          1          12
  11        3          1          12
  12        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     1       6    0    6    6
         2     4       5    0    4    3
         3     9       1    0    4    1
  3      1     1       0    9    6    6
         2     6       0    7    5    6
         3     9       0    2    4    5
  4      1     1       7    0    8    5
         2     6       5    0    5    4
         3     9       0    6    4    3
  5      1     5       8    0    5    5
         2     8       0    3    5    4
         3     8       0    6    3    4
  6      1     4       0    7    6    7
         2     5       7    0    6    5
         3     9       0    5    5    4
  7      1     7       7    0    3    4
         2     9       5    0    3    3
         3    10       0    6    2    1
  8      1     4       7    0    5    4
         2     6       0    7    5    4
         3     7       6    0    4    3
  9      1     7       0    1    2    7
         2     7       4    0    3    6
         3     9       3    0    2    5
 10      1     5       2    0   10    7
         2     5       0    6   10    7
         3    10       2    0   10    6
 11      1     2       0    7    2    6
         2     8       0    4    2    5
         3    10       6    0    1    5
 12      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   20   19   47   47
************************************************************************