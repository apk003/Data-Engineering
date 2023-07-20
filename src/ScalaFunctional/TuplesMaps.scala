package scalaFunctional

object TuplesMaps extends App {

  // sample tuple declaration (ommiting "new Tuple")
  val aTuple = (2, "hello scala")

  // operations on tuples
  println(aTuple._1)
  println(aTuple.copy(_2 = "goodbye java"))
  println(aTuple.swap)

  // sample map declaration
  val aMap: Map[String, Int] = Map()
  val phonebook = Map(("Jim", 555), "Daniel" -> 789).withDefaultValue()
  println(phonebook.contains("Jim"))

  // append map
  val newPairing = "Mary" -> 678
  val newPhonebook = phonebook + newPairing
  println(newPhonebook)

  // functionals on maps
  println(phonebook.map(pair => pair._1.toLowerCase -> pair._2))
  println(phonebook.filterKeys(x => x.startsWith("J")))
  println(phonebook.mapValues(number => "2352" + number))

  // convert maps to other collections
  println(phonebook.toList)
  println(List(("Hi", 542)).toMap)
  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
  println(names.groupBy(name => name.charAt(0)))
}
