package scalaOOP

object Objects extends App {
  // scala does not have class-level functionality (i.e. no "static")
  // instead, objects are used!

  object Person {
    val N_EYES = 2
    def canFly: Boolean = false

    // "factory" method can be used implicitly
    def apply(mother: Person, father: Person): Person = new Person("Bob")
  }

  // objects are single-instance classes
  println(Person.N_EYES)
  println(Person.canFly)

  class Person(val name: String) {
    // "companion" to object Person
  }

  // instances of a class are not equal
  val mary = new Person("mary")
  val john = new Person("mary")
  println(mary == john)

  // you cannot create two separate instances of an object
  val person1 = Person
  val person2 = Person
  println(person1 == person2)

  // apply method is implicitly used!
  val bob = Person(mary,john)
}
