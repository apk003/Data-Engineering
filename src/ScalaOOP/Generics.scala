package scalaOOP

object Generics extends App {
  class MyList[A] {
    // use type A
  }

  class MyMap[Key, Value]

  val listOfIntegers = new MyList[Int]
  val listOfStrings = new MyList[String]

  // generic methods
  object MyList {
    def empty[A]: MyList[A] = ???
  }
  val emptyListOfIntegers = MyList.empty[Int]

  // variance problem
  class Animal
  class Cat extends Animal
  class Dog extends Animal

  // covariance: A -> B implies List[A] -> List[B]
  class CovariantList[+A]
  val animal: Animal = new Cat
  val animalList: CovariantList[Animal] = new CovariantList[Cat]

  // invariance: A -> B does not assume List[A] -> List[B]
  class InvariantList[A]
  val invariantAnimalList: InvariantList[Animal] = new InvariantList[Animal]

  // contravariance: A -> B does not allow List[A] -> List[B]
  class Trainer[-A]
  val trainer: Trainer[Cat] = new Trainer[Animal]

  // bounded types: generic type required to be a class or its extensions
  class Cage[A <: Animal](animal: A)

  // No error because Dog is an Animal
  val cage = new Cage(new Dog)

  // returns error because Car is not an Animal
  /*class Car
  val newCage = new Cage(new Car)*/
}
