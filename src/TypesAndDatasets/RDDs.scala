package BigDataEssentials.TypesAndDatasets

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object RDDs extends App {

  val spark = SparkSession.builder()
    .appName("RDD practice")
    .config("spark.master", "local")
    .getOrCreate()

  /**
   * Exercises
   *
   * 1. Read movies.json as RDD
   * 2. Show distinct genres as an RDD
   * 3. Select movies in Drama genre with IMDB rating > 6
   * 4. Show average rating of movies by genre
   *
   */

  import spark.implicits._
  case class Movie(title: String, genre: String, rating: Double)

  // 1
  val moviesDF = spark.read
    .option("multiline", "true")
    .option("inferSchema", "true")
    .json("src/main/resources/data/movies.json")

  val moviesRDD = moviesDF
    .select(
      col("Title").as("title"),
      col("Major Genre").as("genre"),
      col("IMDB Rating").as("rating")
    ).where(col("genre").isNotNull and col("Rating").isNotNull)
    .as[Movie]
    .rdd

  // 2
  val genresRDD = moviesRDD.map(_.genre).distinct()

  // 3
  val goodDramasRDD = moviesRDD.filter(movie => movie.genre == "Drama" && movie.rating >6)
  goodDramasRDD.toDF.show()

  // 4
  case class GenreAvgRating(genre: String, rating: Double)
  val avgRatingByGenreRDD = moviesRDD.groupBy(_.genre).map {
    case (genre, movies) => GenreAvgRating(genre, movies.map(_.rating).sum / movies.size)
  }

  avgRatingByGenreRDD.toDF.sort(col("rating").desc).show()
}
