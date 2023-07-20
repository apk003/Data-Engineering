package common

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.LoggerFactory
import org.apache.log4j._

object SparkCommon {

  val logger = LoggerFactory.getLogger(getClass.getName)

  def createSparkSession(): SparkSession = {
    logger.info("Creating Spark Session...")
    System.setProperty("hadoop.home.dir", "C:\\winutils")

    val spark = SparkSession
      .builder()
      .appName("SparkScala Common")
      .config("spark.master", "local")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")
    logger.info("Spark Session created successfully.")
    spark
  }

  def createHiveTable (spark: SparkSession): Unit = {
    try {
      logger.warn("createHiveTable method started.")

      spark.sql("create database if not exist fxxcoursedb")
      spark.sql("create table if not exist fxxcoursedb." +
        "fx_course_table(course_id string, course_name string, " +
        "author_name string, no_reviews string)")
      spark.sql("insert into " +
        "fxxcoursedb.fx_course_table VALUES " +
        "(1,'Java','FutureX',45)")
    } catch {
      case e:Exception =>
        logger.warn("Hive table already exists!")
    }
  }

  def writeToHiveTable(spark: SparkSession, df: DataFrame, hiveTable: String): Unit = {
    try {
      val tempView = hiveTable + "tempView"
      df.createOrReplaceTempView("tempView")

      spark.sql("create table " + hiveTable + " as select * from " + tempView)

    } catch {
      case e: Exception =>
      logger.error("Error writing to Hive table"+e.printStackTrace())
    }
  }
}
