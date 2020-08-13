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
"|" splits on pipe charcter


action----

count() - Action count() returns the number of elements in RDD.
collect() - The action collect() is the common and simplest operation that returns our entire RDDs content to driver program.
take(n) - The action take(n) returns n number of elements from RDD
top() - If ordering is present in our RDD, then we can extract top elements from our RDD using top().
countByValue()- The countByValue() returns, many times each element occur in RDD.
reduce() - The reduce() function takes the two elements as input from the RDD and then produces the output of the same type as that of the 	           input elements.
foreach() - When we have a situation where we want to apply operation on each element of RDD, but it should not return value to the driver.


.scala files----- take away from each module

WordCountBetter
					flatMap("\\W+") used to get all words as a separate line

PopularMoviesNicer 
					created a broadcast variable.
					handled character encoding issues - Codec class in scala
					create and populate map using a data file
					loading up a data file without using spark context - here for creating the map

MostPopularSuperhero
					Option data type - return Some or None, in handy when cleaning data up and using flatMap
					rdd.lookup() - lookup returns an array of results

TopTenMostPopularSuperheroes
							rdd.take(x) - to take first x rows
							rdd.lookup() - used to map id to name using two different rdd's

DegreesOfSeparation
					Accumulator is used
					ArrayBuffer is used
					BFS is implemented on superheroes to get degree of separation

MovieSimilarities
					type movierating = (Int, Double) - defines a new data type
					taking args from the user and using command line to execute the spark application
					spark-submit --class com.sundogsoftware.spark.MoviesSimilarities MovieSim.jar 50
					spark-submit --class <packageName.objectName> <name of saved jar file> <argument>
					rdd.join(rdd) -- joins the value of the two rdd's having the same key. ex- before(Int, String) after transformation (Int, String, String)
					rdd.groupByKey() -- returns the tuple with (key, iterable(of all the values associated with the key )). ex - before- (Int, String) after transformation (Int, Iterable(Strings))

					improvements and suggestions 
					1. discard bad ratings only suggest good movies
					2. Different similarity matrix - Pearson correlation coefficient, Jaccard Coefficient, Conditional probability
					3. Adjust the threshold for minimum scores
					4. Use genre information
					5. Invent new similarity matrix that takes co-raters into account

MovieSimilarities1M
					rdd.partitionBy(new HashPartitioner(100)) --partitions the task to utilise parallel computing 
					size of partition should atleast be equal to the number of executor available at the cluster\
					use .partitionBy on a RDD before running a large operation that benefits from partitioning 
						join()
						cogroup()
						groupWith()
						reduceByKey()
						groupByKey()
						combineByKey()
						lookup()

SparkSQL 	
		case class Person(ID:Int, name:String, age:Int, numFriends:Int) -- creates a class with constructor

		 val spark = SparkSession.builder.appName("SparkSQL")
		 .master("local[*]")
		 .config("spark.sql.warehouse.dir", "file:///C:/temp") // Necessary to work around a Windows bug in Spark 2.0.0; omit if you're not on Windows.
        .getOrCreate()
        -- to initialise a spark session

        import spark.implicits._
        val schemaPeople = people.toDS  --whenever converting a rdd to dataset, import spark.implicits._

        schemaPeople.createOrReplaceTempView("people") -- to create a table named "people" from the given dataset

        val teenagers = spark.sql("SELECT * FROM people WHERE age >= 13 AND age <= 19")  -- to make sql queries, returns a dataframe

        always stop the session using .stop() on session variable

DataFrames
			You can use inbuilt function rather typing sql query 
			.select()
			.groupBy()
			.orderBy()
			.filter()
			.show()

MostPopularSuperHeroDs
						val idConnection = pairings.groupBy("heroId").agg(sum("noOfConnections") as ("total")).orderBy(desc("total"))
						-- sum all the fields in the given cllumn grouping them by heroId
						-- here use arg() function

						val key = result.get(0).##()  -- converts any to int type
