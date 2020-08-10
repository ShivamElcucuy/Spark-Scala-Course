package com.sundogsoftware.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._
import shapeless.ops.tuple.Length


object TopTenMostPopularSuperheroes {
  
   // Function to extract hero ID -> hero name tuples (or None in case of failure)
  def parseName(line:String ) : Option[(Int, String)]={
    var fields = line.split('\"')
    if ( fields.length > 1){
      return Some ( fields(0).trim().toInt, fields(1))
    }
    else {
      return None // flatmap will just discard None results, and extract data from Some results.
    }
    
  }
  
  //returns tuples (id, no of connections)
  def countCoOccurrences(line:String) : Option[(Int, Int)] ={
    //separates the string on any no of white spaces
    var fields = line.split("\\s+")
    if(fields.length > 1){
      return Some((fields(0).toInt, fields.length -1))
    }
    else{
      return None
    }
  
  }
  
  //main function where all the actions take place
  def main(args: Array[String]){
    
    //Logging error messages only
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    //create a spark context
    val sc = new SparkContext("local[*]", "TopTenMostPopularSuperheroes")
    
    // Build up a hero ID -> name RDD
    val names = sc.textFile("../marvel-names.txt")
    //contains the tuple (id, name) of superhero
    val idName = names.flatMap(parseName)
    
     // Load up the superhero co-appearance data
    val lines = sc.textFile("../marvel-graph.txt")
    
    // Convert to (heroID, number of connections) RDD
    val pairings = lines.flatMap(countCoOccurrences)
    
    // Combine entries that span more than one line
    val totalFriendsByCharacter = pairings.reduceByKey( (x,y) => x + y )
    
    val fliped = totalFriendsByCharacter.map(x => (x._2, x._1))
    
    //sorting using no of friends
    val sortedDescending = fliped.sortByKey(false)
    val sortedAscending = fliped.sortByKey()
    
      //coverting to name, no of friends
    //val namesFriends = sortedDescending.map(x => (idName.lookup(x._2)(0), x._1))
    
    //getting top ten 
    val topTen = sortedDescending.take(10)
    val lastTen = sortedAscending.take(10)
    
    //printing results
    println("Top ten most popular superheroes")
    println()
   for(fields <- topTen){
     val name = idName.lookup(fields._2)(0)
     val noOfFriends = fields._1
     println(name+ " has "+ noOfFriends + " friends")
   }
    println()
    println()
    println("Top ten least popular superheroes")
    println()
   for(fields <- lastTen){
     val name = idName.lookup(fields._2)(0)
     val noOfFriends = fields._1
     println(name+ " has "+ noOfFriends + " friend")
   }
    
  
    
  
    
   
    
    
    
  }
}