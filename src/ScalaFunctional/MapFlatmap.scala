package scalaFunctional

object MapFlatmap extends App {
  val list = List(1,2,3)

  // map and filter
  println(list.map(_+1))
  println(list.filter(_ % 2 == 0))

  // flatmap will flatten it to 6x1 instead of 3x2
  val toPair = (x: Int) => List(x,x+1)
  println(list.flatMap(toPair))

  val numbers = List(1,2,3,4)
  val chars = List('a','b','c','d')
  val colors = List("black", "white")

  // all combinations
  val combinations = numbers.flatMap(n => chars.flatMap(c => colors.map(color => "" + c+n+ "-" + color)))
  println(combinations)

  // for-comprehensions are much more readable!
  val evenCombinations = for {
    n <- numbers if n % 2 == 0  // filter call
    c <- chars
    color <- colors
  } yield "" + c + n + "-" + color
  println(evenCombinations)

  // alternate map syntax using anonymous functions/lambda
  list.map (x => x*2)
}
