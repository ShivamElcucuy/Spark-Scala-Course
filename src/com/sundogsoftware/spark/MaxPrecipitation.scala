package com.sundogsoftware.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._
import scala.math.max

//Finds the maximum precipitation date in year 1800 by weather station
object MaxPrecipitation {
  
  //method to separate out date, entryType, value of precipitation
  def parseLine(line:String)= {
    val fields = line.split(",")
    val date = fields(1)
    val entryType = fields(2)
    val precipitation = fields(3).toInt
    (date, entryType, precipitation)
  }
  
  
  
    /** Our main function where the action happens */
  def main(args: Array[String]) {
   
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "MaxPrecipitation")
    
    //loads up the data in the given file
    val lines = sc.textFile("../1800.csv")
    
    //get the tuple(date, entryType, precipitation)
    val parsedLines = lines.map(parseLine)
    
    //filter out all but PRCP entry
    val maxPrecipitation = parsedLines.filter(x => x._2 == "PRCP")
   
    //get ( precipitation, date)
    val datePrecipitation = maxPrecipitation.map(x => (x._3.toInt, x._1))
    
    //get the maximum precipitation
    val maxPrecipitationDate = datePrecipitation.max()
   
  
    println(maxPrecipitationDate)
      
  }
}