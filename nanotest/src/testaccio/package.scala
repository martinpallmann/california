package object testaccio {

  implicit class StringOps(s: String) {
    def - (f: => Unit): Unit = {
      TestRunner.register(s, () => f)
    }
  }
}
