package BigDataEssentials.TypesAndDatasets

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object ManagingNulls extends App {

  val spark = SparkSession.builder()
    .appName("Managing Nulls")
    .config("spark.master", "local")
    .getOrCreate()

  val moviesDF = spark.read
    .option("inferSchema", "true")
    .option("multiline", "true")
    .json("src/main/resources/data/movies.json")

  // select first non-null value
  moviesDF.select(
    col("Title"),
    col("Rotten Tomatoes Rating"),
    col("IMDB Rating"),
    coalesce(col("Rotten Tomatoes Rating"), col("IMDB Rating")*10)
  ).show()

  // check for nulls
  moviesDF.select("*").where(col("Rotten Tomatoes Rating").isNull)

  // nulls when ordering
  moviesDF.orderBy(col("IMDB Rating").desc_nulls_last)

  // remove and replace nulls
  moviesDF.select("Title", "IMDB Rating").na.drop()
  moviesDF.na.fill(0, List("IMDB Rating", "Rotten Tomatoes Rating"))
  moviesDF.na.fill(Map(
    "IMDB Rating" -> 0,
    "Rotten Tomatoes Rating" -> 10,
    "Director" -> "Unknown"
  )).show()
}
