package BigDataEssentials.DataFrames

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._

object DFBasics extends App {

  val spark = SparkSession.builder()
    .appName("DataFrames Basics")
    .config("spark.master", "local")
    .getOrCreate()

  val df = spark.read
    .option("multiline", "true")
    .json("src/main/resources/data/cars.json")

  // various console output actions
  df.show()
  df.printSchema()
  df.take(10).foreach(println)

  // spark types
  val longType = LongType

  // explicitly define cars schema
  val carsSchema = StructType(Array(
    StructField("Name", StringType),
    StructField("Miles_per_Gallon", IntegerType),
    StructField("Cylinders", IntegerType),
    StructField("Displacement", IntegerType),
    StructField("Horsepower", IntegerType),
    StructField("Weight_in_lbs", IntegerType),
    StructField("Acceleration", DoubleType),
    StructField("Year", StringType),
    StructField("Origin", StringType),
  ))

  // inferred cars schema
  val carsDFSchema = df.schema

  // read with schema
  val carsDFWithSchema = spark.read
    .option("multiline", "true")
    .json("src/main/resources/data/cars.json")

  // create row by hand
  // note that individual rows do not have schemas
  val myRow = Row("chevrolet chevelle malibu",18,8,307,130,12.0,"1970-01-01", "USA")

  // create DF from tuples
  val cars = Seq(
    ("chevrolet fakecar1",18,8,307,130,12.0,"1970-01-01", "USA"),
    ("chevrolet fakecar2",18,8,307,130,12.0,"1970-01-01", "USA")
  )
  val manualCarsDF = spark.createDataFrame(cars)

  // create DF with implicits
  import spark.implicits._
  val manualCarsDFImplicits = cars.toDF("Name", "MPG", "Cylinders", "Displacement", "HP", "Acceleration", "Year", "Country")

  manualCarsDF.printSchema()
  manualCarsDFImplicits.printSchema()


  /**
   * Exercise:
   * 1) Create a DF describing smartphones
   *  - make
   *  - model
   *  - OS
   *  - camera mpx
   *
   * 2) Read another file from data folder
   *  - print schema
   *  - count num rows with method
   */

  // 1
  val smartphones = Seq(
    ("Google", "Pixel", "Android", 12),
    ("Apple", "iPhone", "iOS", 13),
    ("Samsung", "Galaxy", "Android", 12),
    ("Nokia", "3310", "Nokia", 0)
  )

  val smartphonesDF = smartphones.toDF("Make", "Model", "Platform", "CameraMegapixels")
  smartphonesDF.show()

  // 2
  val moviesDF = spark.read
    .option("inferSchema", "true")
    .json("src/main/resources/data/movies.json")

  moviesDF.printSchema()
  println(s"The Movies dataset has ${moviesDF.count()} rows")
}
