package testaccio

import scala.util.control.NonFatal

object TestRunner {

  private var registry: Map[String, () => Unit] = Map.empty
  def register(name: String, f: () => Unit): Unit =
    registry = registry + (name -> f)

  def main(args: Array[String]): Unit = {

    def ok(name: String): Unit                = println(s"ok:    $name")
    def err(name: String, t: Throwable): Unit = println(s"error: $name")

    println(s"running ${registry.size} tests.")

    registry.foreach {
      case (name, fn) =>
        try {
          fn()
          ok(name)
        } catch {
          case NonFatal(e) => err(name, e)
        }
    }
  }
}

