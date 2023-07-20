package BigDataEssentials.DataFrames

import common.PostgresCommon
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.types._

object DataSources extends App {

  val spark = SparkSession.builder()
    .appName("Data Sources and Formats")
    .config("spark.master", "local")
    .getOrCreate()

  val carsSchema = DFBasics.carsSchema

  /**
    Reading a DataFrame requires:
      - format
      - schema (inferred or explicit)
      - options (default or explicit)
   */

  val carsDF = spark.read
    .schema(carsSchema)
    //.option("inferSchema", "true")
    .option("multiline", "true")
    .json("src/main/resources/data/cars.json")
  //carsDF.show() // validate DF is read correctly
  //carsDF.printSchema()

  // alternative reading of options
  val carsDFOptionsMap = spark.read
    .options(Map(
      "multiline" -> "true",
      "path" -> "src/main/resources/data/cars.json",
      "interSchema" -> "true"
    )).json()

  /*
    Writing DFs requires:
      - format
      - save mode
      - path
      - options (default or explicit)
   */

  carsDF.write
    .mode(SaveMode.Overwrite)
    .json("src/main/resources/data/cars_duplicate.json")


  val carsAltSchema = StructType(Array(
    StructField("Name", StringType),
    StructField("Miles_per_Gallon", IntegerType),
    StructField("Cylinders", IntegerType),
    StructField("Displacement", IntegerType),
    StructField("Horsepower", IntegerType),
    StructField("Weight_in_lbs", IntegerType),
    StructField("Acceleration", DoubleType),
    StructField("Year", DateType),
    StructField("Origin", StringType),
  ))
  // JSON flags
  spark.read
    .schema(carsAltSchema)
    .option("dateFormat", "YYYY-MM-dd")
    .option("allowSingleQuotes", "true")
    .option("compression", "uncompressed")
    .json("src/main/resources/data/cars.json")

  // CSV flags
  val stocksSchema = StructType(Array(
    StructField("symbol", StringType),
    StructField("date", DateType),
    StructField("price", DoubleType)
  ))
  spark.read
    .schema(stocksSchema)
    .option("dateFormat", "MMM dd YYYY")
    .option("header", "true")
    .option("sep", ",")
    .option("nullValue", "")
    .csv("src/main/resources/data/stocks.csv")

  // Parquet
  carsDF.write
    .mode(SaveMode.Overwrite)
    .parquet("src/main/resources/data/cars.parquet")

  /*
    Reading text files:
      - spark.read.text
   */

  // read from DB
  val pgTable = "courseschema.courses"
  val pgConnectionProperties = PostgresCommon.connectToDB()
  val courses = spark.read
    .option("driver", "org.postgresql.Driver")
    .jdbc("jdbc:postgresql://localhost:5432/training", pgTable, pgConnectionProperties)

  courses.show()

  /**
   * Exercise: read the movies DF, then write it as
   *  - tab-separated values file
   *  - snappy Parquet
   *  - table "public.movies in Postgres DB
   */

  val moviesDF = spark.read.json("src/main/resources/data/movies.json")

  // TSV
  moviesDF.write
    .option("header", "true")
    .option("sep", "\t")
    .csv("src/main/resources/data/movies.csv")

  // Parquet
  moviesDF.write.save("src/main/resources/data/movies.parquet")

  // DB
  moviesDF.write
    .option("driver", "org.postgresql.Driver")
    .jdbc("jdbc:postgresql://localhost:5432/training", "public.movies", pgConnectionProperties)

  // Check pgadmin for public.movies!
}
