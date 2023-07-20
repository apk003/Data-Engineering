package scalaFunctional

import scala.util._

object ErrorHandling extends App {

  // create success and failure
  val success = Success(3)
  val failure = Failure(new RuntimeException("failed"))

  println(success)
  println(failure)

  def unsafeMethod(): String = throw new RuntimeException("no")

  // try by apply method
  val potentialFailure = Try(unsafeMethod())
  println(potentialFailure)

  // alternate syntax
  val anotherPotentialFailure = Try {}

  println(potentialFailure.isSuccess)

  // orElse method
  def backupMethod(): String = "valid"
  val fallbackTry = Try(unsafeMethod()).orElse(Try(backupMethod()))

  // alternate syntax: orElse as keyword!
  def betterUnsafeMethod(): Try[String] = Failure(new RuntimeException)
  def betterBackupMethod(): Try[String] = Success("valid")
  val betterFallback = betterUnsafeMethod() orElse betterBackupMethod()

  // functionals with Try
  println(success.map(_*2))
  println(success.flatMap(x => Success(x*10)))
  println(success.filter(_ > 10))



  // Example
  val hostname = "localhost"
  val port ="8080"
  def renderHTML(page: String) = println(page)

  class Connection {
    def get(url: String): String = {
      val random = new Random(System.nanoTime())
      if (random.nextBoolean()) "<html>...</html>"
      else throw new RuntimeException("Connection interrupted")
    }

    def getSafe(url: String): Try[String] = Try(get(url))
  }

  object HttpService {
    val random = new Random(System.nanoTime())

    def getConnection(host: String, port: String): Connection = {
      if (random.nextBoolean()) new Connection
      else throw new RuntimeException("Someone else took the port")
    }

    def getSafeConnection(host: String, port: String): Try[Connection] = Try(getConnection(host, port))
  }

  // if html page retrieved from connection, call renderHTML
  val possibleConnection = HttpService.getSafeConnection(hostname, port)
  val possibleHTML = possibleConnection.flatMap(connection => connection.getSafe("/home"))
  possibleHTML.foreach(renderHTML)

  // alternate syntax
  HttpService.getSafeConnection(hostname, port)
    .flatMap(connection => connection.getSafe("/home"))
    .foreach(renderHTML)

  // for-comprehension syntax
  for {
    connection <- HttpService.getSafeConnection(hostname, port)
    html <- connection.getSafe("/home")
  } renderHTML(html)
}
