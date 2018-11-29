package california.parser

import akka.stream.scaladsl.Source
import nanotest.TestSuite

object UnfolderTest extends WithProcessor[String, Char, Char] with TestSuite {

  val processor = () => new Unfolder
  val zero = ""
  val fn = _ + _

  test("unfold 1") {
    assert(process(Source("Hallo \r\n Welt")) == "Hallo Welt")
  }

  test("unfold 2") {
    assert(process(Source("Hallo \r\n\tWelt")) == "Hallo Welt")
  }

  test("unfold 3") {
    assert(process(Source("Hallo \r\nWelt")) == "Hallo \r\nWelt")
  }

  test("unfold 3") {
    assert(process(Source("Hallo \r Welt")) == "Hallo \r Welt")
  }

  test("unfold 4") {
    assert(process(Source("Hallo \n Welt")) == "Hallo \n Welt")
  }

}
