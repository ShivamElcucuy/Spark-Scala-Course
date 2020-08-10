# Spark-Scala-Course

Shivam 
learning Apache spark with scala - Hands on with big data ----Udemy

regular expressions ------

","   splits on commas
"\t"  splits on tab
"\\W+"  word by word split
"\\s+" split on any no of white spaces
'\"' splits on double quotes
" " splits on space charcter


action----

count() - Action count() returns the number of elements in RDD.
collect() - The action collect() is the common and simplest operation that returns our entire RDDs content to driver program.
take(n) - The action take(n) returns n number of elements from RDD
top() - If ordering is present in our RDD, then we can extract top elements from our RDD using top().
countByValue()- The countByValue() returns, many times each element occur in RDD.
reduce() - The reduce() function takes the two elements as input from the RDD and then produces the output of the same type as that of the 	           input elements.
foreach() - When we have a situation where we want to apply operation on each element of RDD, but it should not return value to the driver.


.scala files-----

WordCountBetter
					flatMap("\\W+") used to ger all words as a separate line

PopularMoviesNicer 
					created a broadcast variable.
					handled character encoding issues
					create and populate map using a data file
					loading up a data file without using spark context - here for creating the map

MostPopularSuperhero
					Option data type - return Some or None, in handy when cleaning data up and using flatMap
					rdd.lookup() - lookup returns an array of results

TopTenMostPopularSuperheroes
							rdd.take(x) - to take first x rows
							rdd.lookup() - used to map id to name using two different rdd's