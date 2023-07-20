package BigDataEssentials.DataFrames

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Aggregations extends App {

  val spark = SparkSession.builder()
    .appName("Aggregations")
    .config("spark.master", "local")
    .getOrCreate()

  val moviesDF = spark.read
    .option("inferSchema", "true")
    .option("multiline", "true")
    .json("src/main/resources/data/movies.json")

  // counting
  val genres = moviesDF.select(countDistinct(col("Major Genre")).as("Genre Count"))
  genres.show()
  moviesDF.select(count("*")).show() // includes null

  // min, max, sum
  val minRatingDF = moviesDF.select(min(col("IMDB Rating")))
  moviesDF.select(sum(col("US Gross")))
  moviesDF.select(avg(col("Rotten Tomatoes Rating")))

  // grouping
  val countByGenreDF = moviesDF
    .filter(col("Major Genre") =!= "null")
    .groupBy(col("Major Genre"))
    .count() // SELECT COUNT(*) FROM moviesDF GROUP BY Major_Genre
  countByGenreDF.show()

  /**
   * Exercises
   *
   * 1. Sum profits of all movies in DF
   * 2. Count distinct directors
   * 3. Show mean and stddev of US gross revenue
   * 4. Compute average IMDB rating and US gross revenue per director
   *
   */

  // 1
  moviesDF.select((col("US Gross") + col("Worldwide Gross") + col("US DVD Sales"))
    .as("Total Gross"))
    .select(sum(col("Total Gross")))
    .show()

  // 2
  moviesDF.select(countDistinct(col("Director")))
    .show()

  // 3
  moviesDF.select(
    mean(col("US Gross")),
    stddev(col("US Gross"))
  ).show()

  // 4
  moviesDF.groupBy(col("Director"))
    .agg(
      avg("IMDB Rating").as("Avg Rating"),
      sum("US Gross").as("Total US Gross")
    )
    .orderBy(col("Avg Rating").desc_nulls_last)
    .show()
}
