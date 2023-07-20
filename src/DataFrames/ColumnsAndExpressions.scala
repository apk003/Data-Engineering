package BigDataEssentials.DataFrames

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions._

object ColumnsAndExpressions extends App {

  val spark = SparkSession.builder()
    .appName("DF Columns and Expressions")
    .config("spark.master", "local")
    .getOrCreate()

  val carsDF = spark.read
    .option("inferSchema", "true")
    .option("multiline", "true")
    .json("src/main/resources/data/cars.json")

  val name_column = carsDF.col("Name")
  val carNamesDF = carsDF.select(name_column)
  carNamesDF.show()

  // various select methods for columns
  import spark.implicits._
  carsDF.select(
    carsDF.col("Name"),
    col("Acceleration"),
    column("Weight_in_lbs"),
    'Year,
    $"Horsepower",
    expr("Origin")
  )

  // select multiple columns by string
  // Note that this is incompatible with column notation
  carsDF.select("Name", "Year")

  // Expression
  val simpleExpression = carsDF.col("Weight_in_lbs")
  val weightInKgExpression = carsDF.col("Weight_in_lbs") / 2.2

  val carsWithWeights = carsDF.select(
    col("Name"),
    col("Weight_in_lbs"),
    weightInKgExpression.as("Weight_in_kg"),
    expr("Weight_in_lbs / 2.2").as("Weight_in_kg_2")
  )

  // selectExpr
  val carsWithSelectExprWeightsDF = carsDF.selectExpr(
    "Name",
    "Weight_in_lbs",
    "Weight_in_lbs / 2.2"
  )

  // DF processing
  val carsWithKg3DF = carsDF.withColumn("Weight_in_kg_3", col("Weight_in_lbs") / 2.2)

  val carsWithColumnRenamed = carsDF.withColumnRenamed("Weight_in_lbs", "Weight in pounds")
  carsWithColumnRenamed.selectExpr("`Weight in pounds`")
  carsWithColumnRenamed.drop("Cylinders", "Replacement")

  // filtering
  val EUCarsDF = carsDF.filter(col("Origin") =!= "USA")
  val EUSportCarsDF = carsDF.where(col("Origin") =!= "USA"
    and col("Horsepower") > 150)
  val americanPowerfulCarsDF = carsDF.filter("Origin = 'USA' and Horsepower > 150")

  // adding rows
  val newRow = Seq(("fake car", 18, 8, 307, 130, 3504, 12,"1970-01-01","USA"))
  val moreCarsDF = carsDF.union(newRow.toDF()).show()

  /**
   * Exercises
   *
   * 1. Read movies DF and select 2 columns
   * 2. Create another column summing up total profits
   * 3. Select all comedy movies with IMDB rating above 6
   *
   */

  // 1
  val moviesDF = spark.read.option("inferSchema", "true")
    .json("src/main/resources/data/movies.json").cache()
  val moviesUSGrossDF = moviesDF.select("Title", "US_Gross")

  // 2
  val totalProfitsDF = moviesDF.select(
    col("Title"),
    col("US_Gross"),
    col("Worldwide_Gross"),
    col("US_DVD_Sales"),
    (col("US") + col("Worldwide_Gross") + col("US_DVD_Sales")).as("Total_Gross")
  )

  // 3
  val goodComedyDF = moviesDF.select("Title", "IMDB_Rating")
    .where(col("Major_Genre") === "Comedy" and col("IMDB_Rating") > 6)
}
