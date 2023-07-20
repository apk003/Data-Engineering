package BigDataEssentials.TypesAndDatasets

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object ComplexTypes extends App {

  val spark = SparkSession.builder()
    .appName("Complex Data Types")
    .config("spark.master", "local")
    .getOrCreate()

  val moviesDF = spark.read
    .option("multiline", "true")
    .option("inferSchema", "true")
    .json("src/main/resources/data/movies.json")

  // Dates
  val moviesWithReleaseDates = moviesDF
    .select(col("Title"), to_date(col("Release Date"), "MMM d yyyy").as("Actual Release"))

  moviesWithReleaseDates
    .withColumn("Today", current_date())
    .withColumn("Right Now", current_timestamp())
    .withColumn("Movie Age", datediff(col("Today"), col("Actual Release")) / 365)
    .show()

  /**
   * Exercise
   * 1. How do we deal with multiple date formats?
   * 2. Read stocks DF and parse dates
   */

  // 1 - parse DF more than once and use .union()

  // 2
  val stocksDF = spark.read
    .options(Map(
      "inferSchema" -> "true",
      "header" -> "true"
    )).csv("src/main/resources/data/stocks.csv")

  val stocksDFwithDates = stocksDF
    .withColumn("actual_date", to_date(col("date"), "MMM d yyyy"))

  stocksDFwithDates.show()

  // structures
  moviesDF.select(col("Title"), struct(col("US Gross"), col("Worldwide Gross")).as("Profit")) // make tuple
    .select(col("Title"), col("Profit").getField("US Gross").as("US Profit")) // extract US Gross from tuple Profit

  // alternative - expr
  moviesDF.selectExpr("Title", "(`US Gross`, `Worldwide Gross`) as Profit")
    .selectExpr("Title", "Profit.`US Gross`")
    .show()

  // arrays
  val moviesWithWords = moviesDF.select(col("Title"), split(col("Title"), " |,").as("Title Words")) // array

  moviesWithWords.select(
    col("Title"),
    expr("`Title Words`[0]"),
    size(col("Title Words")),
    array_contains(col("Title Words"), "Love")
  ).show()
}
