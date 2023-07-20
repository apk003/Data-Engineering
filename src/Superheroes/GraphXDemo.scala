package superheroes

import org.apache.spark.SparkContext

object GraphXDemo {

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local[*]", "GraphX")
  }
}
