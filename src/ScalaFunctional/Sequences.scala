package scalaFunctional

import scala.util.Random

object Sequences extends App {

  // Seq is very general
  val aSequence = Seq(1,2,3,4)

  // Seq has a factory function turning it into a list!
  println(aSequence)

  // various Seq operations
  println(aSequence.reverse)
  println(aSequence ++ Seq(7,5,6))
  println(aSequence.sorted)

  // Ranges are general iterables
  val aRange: Seq[Int] = 1 until 10
  aRange.foreach(println)

  // how to call range
  (1 to 10).foreach(x => println("Hello"))

  // lists are FAST immutable extensions to LinearSeq
  // especially fast compared to python lists
  val aList = List(1,2,3)
  val prepended = 42 +: aList :+ 89
  println(prepended)

  val apples5 = List.fill(5)("apple")
  println(apples5)
  println(aList.mkString("-|-"))

  // Arrays are mutable and have fast indexing
  val numbers = Array(1,2,3,4)
  val threeElements = Array.ofDim[Int](3)
  threeElements.foreach(println)

  numbers(2) = 0
  println(numbers.mkString(" "))

  val numbersSeq: Seq[Int] = numbers // implicit conversion
  println(numbersSeq)

  // vectors are extremely efficient immutable sequences
  val vector: Vector[Int] = Vector(1,2,3)
  println(vector)

  val maxRuns = 1000
  val maxCapacity = 1000000

  def getWriteTime(collection: Seq[Int]): Double = {
    val r = new Random
    val times = for {
      it <- 1 to maxRuns
    } yield {
      val currentTime = System.nanoTime()
      collection.updated(r.nextInt(maxCapacity), 0)
      System.nanoTime() - currentTime
    }

    times.sum * 1.0 / maxRuns
  }

  val numbersList = (1 to maxCapacity).toList
  val numbersVector = (1 to maxCapacity).toVector

  // pros: keeps reference to tail
  // cons: updating element in the middle takes long
  println(getWriteTime(numbersList))

  // pros: low depth of tree
  // cons: needs to replace entire 32-element chunk
  println(getWriteTime(numbersVector))
}
