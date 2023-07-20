package BigDataEssentials.TypesAndDatasets

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, Encoders, SparkSession}

object DataSets extends App {

  val spark = SparkSession.builder()
    .appName("Datasets")
    .config("spark.master", "local")
    .getOrCreate()

  val numbersDF = spark.read
    .option("inferSchema", "true")
    .option("header", "true")
    .csv("src/main/resources/data/numbers.csv")

  implicit val intEncoder = Encoders.scalaInt
  val numbersDS: Dataset[Int] = numbersDF.as[Int]
  numbersDS.filter(_ > 100)

  // complex dataset
  // 1 - define case class
  case class Car(
                  Name: String,
                  Miles_per_Gallon: Option[Double],
                  Cylinders: Long,
                  Displacement: Double,
                  Horsepower: Option[Long],
                  Acceleration: Double,
                  Origin: String
                )

  // 2 - read dataframe
  def readDF(filename: String) = spark.read
    .option("inferSchema", "true")
    .option("multiline", "true")
    .json(s"src/main/resources/data/$filename")
  val carsDF = readDF("cars.json")

  // 3 - define encoder
  import spark.implicits._

  // 4 - convert DF to DS
  val carsDS = carsDF.as[Car]

  // DS collection function
  numbersDS.filter(_ < 100).show()

  // map, flatmap, fold, reduce, etc.
  val carNamesDS = carsDS.map(car => car.Name.toUpperCase())
  carNamesDS.show()

  /**
   * Exercises
   *
   * 1. Count cars
   * 2. Count powerful cars (HP > 140)
   * 3. Compute avg HP
   *
   */

  // 1
  val carsCount = carsDS.count
  println(carsCount)

  // 2
  println(carsDS.filter(_.Horsepower.getOrElse(0L) > 140).count)

  // 3
  println(carsDS.map(_.Horsepower.getOrElse(0L)).reduce(_ + _) / carsCount)
}
