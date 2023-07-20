package scalaFunctional

object AnonymousFunctions extends App{

  // anonymous functions are like lambdas
  val doubler: Int => Int = (x: Int) => x * 2
  val justDoSomething: () => Int = () => 3
  val adder: (Int, Int) => Int = (a: Int, b: Int) => a + b

  println(justDoSomething())

  // alternate style
  val stringToInt = { (str: String) =>
    str.toInt
  }

  // simpler syntax
  val niceIncrementer: Int => Int = _ + 1
  val niceAdder: (Int, Int) => Int = _ + _
}
