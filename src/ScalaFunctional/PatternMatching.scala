package scalaFunctional

object PatternMatching extends App {

  val x: Any = "Scala"
  val constants = x match {
    case 1 => "a number"
    case "Scala" => "THE Scala"
    case true => "The Truth"
    case PatternMatching => "A singleton object"
  }


  val matchAnything = x match {
    case _ =>
  }

  val matchVariable = x match {
    case something => s"I've found $something"
  }

  val tuple = (1,2)
  val matchTuple = tuple match {
    case (1,1) =>
    case (something, 2) => s"I've found $something"
  }

  // nesting pattern matches
  val nestedTuple = (1, (2, 3))
  val matchNestedTuple = nestedTuple match {
    case (_, (2, v)) =>
  }

  // list extractor
  val standardList = List(1,2,3,4)
  val standardListMatching = standardList match {
    case List(1, _, _, _) =>
    case List(1, _*) => // arbitrary length
    case 1 :: List(_) => // infix pattern
    case List(1,2,3) :+ 30 =>
  }

  // match bounds
  val unknown: Any = 2
  val unknownMatch = unknown match {
    case list: List[Int] => // explicit type specifier
    case _ =>
  }
}
