package com.sundogsoftware.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.sql._
import org.apache.log4j._
import scala.io.Source
import java.nio.charset.CodingErrorAction
import scala.io.Codec
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.sum

object MostPopularSuperHeroDs {
  
  case class Hero(heroId:Int, noOfConnections:Int)
  
  
  
  // Function to extract the hero ID and number of connections from each line
  def countCoOccurrences(line: String):Hero = {
    var elements = line.split("\\s+")
    val hero: Hero = Hero( elements(0).toInt, (elements.length - 1).toInt )
    return hero
  }
  
  //returns heroId , heroname  tuple
   def parseNames(line: String) : Option[(Int, String)] = {
    var fields = line.split('\"')
    if (fields.length > 1) {
      return Some(fields(0).trim().toInt, fields(1))
    } else {
      return None // flatmap will just discard None results, and extract data from Some results.
    }
  }
   
  
 
  /** Our main function where the action happens */
  def main(args: Array[String]) {
   
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
     val spark = SparkSession
      .builder
      .appName("SparkSQL")
      .master("local[*]")
      .config("spark.sql.warehouse.dir", "file:///C:/temp") // Necessary to work around a Windows bug in Spark 2.0.0; omit if you're not on Windows.
      .getOrCreate()
    
    
    val names = spark.sparkContext.textFile("../marvel-names.txt")
    val namesRdd = names.flatMap(parseNames)
   
    
    // Load up the superhero co-appearance data
    val lines = spark.sparkContext.textFile("../marvel-graph.txt")
    
    // Convert to (heroID, number of connections) RDD and finally converts it to DS
    import spark.implicits._
    val pairings = lines.map(countCoOccurrences).toDS().cache()
    
   
    
   
    //getting the count of no of connections of each heroes using agg and sum
    val idConnection = pairings.groupBy("heroId").agg(sum("noOfConnections") as ("total")).orderBy(desc("total"))
    
    // show top 20 ids
   // pairings.select("heroId", "noOfConnections").show()
    idConnection.show()
    
    //take the most famous heroId
    val mostFamous = idConnection.take(10)
    
    
    
    println
    for (result <- mostFamous) {
      // result is just a Row at this point; we need to cast it back.
      // Each row has movieID, count as above.
      val key = result.get(0).##()
      
      val name = namesRdd.lookup(key)(0)
      println(name + " has "+ result.get(1) + " connections")
     }
    
   
    spark.stop()
  }
}