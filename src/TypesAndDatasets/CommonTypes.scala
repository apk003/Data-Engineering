package BigDataEssentials.TypesAndDatasets

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object CommonTypes extends App {

  val spark = SparkSession.builder()
    .appName("Common Spark Types")
    .config("spark.master", "local")
    .getOrCreate()

  val moviesDF = spark.read
    .option("multiline", "true")
    .option("inferSchema", "true")
    .json("src/main/resources/data/movies.json")

  // add plain value to DF
  moviesDF.select(col("Title"), lit(47).as("plain_value")).show()

  // booleans
  val dramaFilter = col("Major Genre") equalTo "Drama"
  val goodRatingFilter = col("IMDB Rating") > 7.0
  moviesDF.select(col("Title"), (dramaFilter and goodRatingFilter).as("good_movie")).show()

  // numbers
  val moviesAvgRatingsDF = moviesDF.select(col("Title"), (col("Rotten Tomatoes Rating") / 10 + col("IMDB Rating")) / 2)

  // pearson correlation
  println(moviesDF.stat.corr("Rotten Tomatoes Rating", "IMDB Rating"))

  // strings
  val carsDF = spark.read
    .option("inferSchema", "true")
    .option("multiline", "true")
    .json("src/main/resources/data/cars.json")

  carsDF.select(initcap(col("Name"))).show()

  carsDF.select("*").where(col("Name").contains("volkswagen"))

  val regexString = "volkswagen|vw"
  carsDF.select(
    col("Name"),
    regexp_extract(col("Name"), regexString, 0).as("regex_extract")
  ).where(col("regex_extract") =!= "").drop("regex_extract").show()

  carsDF.select(
    col("Name"),
    regexp_replace(col("Name"), regexString, "People's Car").as("regex_replace")
  ).show()

  /**
   * Exercise
   *
   * Filter cars DF by list of car names obtained by API call
   *  - contains
   *  - regex
   *
   */

  def getCarNames: List[String] = List("Volkswagen", "Mercedes-Benz", "Ford")

  // "volkswagen|mercedes-benz|ford"
  val complexRegex = getCarNames.map(_.toLowerCase()).mkString("|")

  // regex solution
  carsDF.select(
    col("Name"),
    regexp_extract(col("Name"), complexRegex, 0).as("regex_extract")
  ).where(col("regex_extract") =!= "")
    .drop("regex_extract")
    .show()

  // contains volkswagen, mercedes-benz, or ford
  val carNameFilters = getCarNames.map(_.toLowerCase()).map(name => col("Name").contains(name))

  // contains solution
  val namesFilter = carNameFilters.fold(lit(false))((combinedFilter, newCarNameFilter) => combinedFilter or newCarNameFilter)
  carsDF.filter(namesFilter).show()
}
