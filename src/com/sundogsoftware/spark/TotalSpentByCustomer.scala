package com.sundogsoftware.spark


import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._


object TotalSpentByCustomer {
  
  //parsing the lines to get (customerId, moneySpent) tuple
  def parseLine(line:String)={
    //splitting by commas
    val field = line.split(",")
    //setting up customerId
    val customerId = field(0).toInt
    //setting up monetSpent
    val moneySpent = field(2).toFloat
    
    //returning tuple
    (customerId, moneySpent)
  }
  
  //main function where all the action takes place
  def main(args: Array[String]){
    
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    // Create a SparkContext using the local machine
    val sc = new SparkContext("local[*]", "TotalSpentByCostumer")   
    
    // Load each line of the source data into an RDD
    val lines = sc.textFile("../customer-orders.csv")
    
    //setting up tuple (customerId, moneySpent) and summing up the money spent by each customer using Id
    val customerMoney = lines.map(parseLine).reduceByKey((x,y) => (x+y))
    
    //reversing the tuple to apply sorting
    val moneyCustomer= customerMoney.map(x => (x._2.toFloat , x._1.toInt))
    
    //sorting using money spent in descending order
    val sorted = moneyCustomer.sortByKey(false)
    
    //collecting the final results
    val collectingResults = sorted.collect()
    
    //printing the sorted results
    for(field <- collectingResults){
      val spent = field._1
      val custId = field._2
      println("customerId : " + custId + " total money spent = " + spent)
      //println(s"$custId : $spent")
    }
    
    //collectingResults.foreach(println)
    
  }
  
}