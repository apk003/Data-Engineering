package pipeline

import common.{PostgresCommon, SparkCommon}
import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory

object BasicPipeline {


  System.setProperty("hadoop.home.dir", "C:\\winutils")
  private val logger = LoggerFactory.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {
  try {
    logger.info("Initiating main method...")
    val spark: SparkSession = SparkCommon.createSparkSession()
    //val sample = Seq((1, "Spark"), (2, "Big Data"))
    //val df = spark.createDataFrame(sample).toDF("course id", "course name")

    //df.show()

    val pgTable = "courseschema.courses"
    val pgConnectionProperties = PostgresCommon.connectToDB()
    val pgCourseDataFrame = spark.read.jdbc("jdbc:postgresql://localhost:5432/training", pgTable, pgConnectionProperties)

    logger.info("Dataframe fetch successful.")
    pgCourseDataFrame.show()

    logger.info("Creating Hive Table...")
    SparkCommon.createHiveTable(spark)

    logger.info("Writing to Hive Table...")

    SparkCommon.writeToHiveTable(spark, pgCourseDataFrame, "fxxcoursedb")
    logger.info("Finished writing to Hive Table")
  } catch {
    case e:Exception =>
      logger.error("An error has occurred in the main method")
  }
    logger.info("Main method executed successfully")
    logger.info("Congratulations, you have built your first data pipeline in Scala!\n\n")
  }
}