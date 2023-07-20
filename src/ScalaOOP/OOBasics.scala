package scalaOOP

object OOBasics extends App {
  val person = new Person("Anthony", 20)

  //println(person.age) // REQUIRES val keyword to become attribute!
  //person.greet("Daniel")
  person.greet()

  // infix notation for single parameter methods
  println(person is 20)

  val author = new Writer("Charles", "Dickens", 1812)
  val duplicate = new Writer("Charles", "Dickens", 1812)
  val novel = new Novel("Great Expectations", 1861, author)

  // Charles Dickens wrote Great Expectations at 49 years old
  println(novel.authorAge)

  // Both instances work due to Writer.equals
  println(novel.isWrittenBy(author))
  println(novel.isWrittenBy(duplicate))

  // Counting to 3
  val counter = new Counter
  counter.increment.increment.increment.print

  // increment until 10
  counter.increment(10).print

  // decrement to -5 or until 0 is hit
  counter.decrement(-5).print

  val x = -1
  val y = 1.unary_-
  print(x==y)
}

// In scala, class declarations act as constructors
class Person(name: String, val age: Int) {

  // simple class method
  def greet(name: String): Unit = println(s"${this.name} says: Hi, $name")

  // overload for different use
  def greet(): Unit = println(s"Hi, I am $name")

  // multiple constructors
  def this(name: String) = this(name, 0)
  def this() = this("John Doe")

  def is(num: Int): Boolean = num == age
}

class Writer(firstName: String, surname: String, val year: Int) {

  // operators work as methods
  def fullName: String = firstName.+(" ").+(surname)

  // writers are equal if they have the same name and birth date
  def equals(author: Writer): Boolean = {
    author.fullName == this.fullName && author.year == this.year
  }
}

class Novel(name: String, year: Int, author: Writer) {
  def authorAge = {year - author.year}
  def isWrittenBy(author: Writer) = author.equals(this.author)
  def copy(newYear: Int): Novel = new Novel(name, newYear, author)
}

class Counter(val count: Int = 0) {
  def print = println(s"Counter: $count")
  def increment = new Counter(count+1) // objects are immutable
  def decrement = new Counter(count-1)

  // increment recursively to n
  def increment(n: Int): Counter = {
    if (n <= 0) this
    else increment.increment(n - 1)
  }

  // decrement recursively to n
  def decrement(n: Int): Counter = {
    if (n <= 0) this
    else decrement.decrement(n - 1)
  }
}