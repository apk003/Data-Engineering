package scalaOOP

object Inheritance extends App {
  class Animal {
    val creatureType = "wild"
    def eat = println("nomnom")
  }

  class Cat extends Animal {
    def crunch = {
      eat
      println("crunch crunch")
    }
  }

  val cat = new Cat
  cat.eat

  // constructors
  class Person(name: String, age: Int) {
    def this(name: String) = this(name,0)
  }
  class Adult(name: String, age: Int, idCard: String) extends Person(name)

  // override methods and attributes
  class Dog(override val creatureType: String) extends Animal {
    override def eat = println("crunch, crunch")
  }

  val dog = new Dog("domestic")
  val dog2 = new Dog("K9")
  dog.eat
  println(s"The first dog is ${dog.creatureType} and the other is ${dog2.creatureType}")

  // polymorphism
  val unknownAnimal: Animal = new Dog("K9")
  unknownAnimal.eat

  // super
  class Lynx extends Cat {
    override def eat = {
      super.eat
    }
  }

  val lynx = new Lynx
  lynx.eat
}
