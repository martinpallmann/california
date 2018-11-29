package california.parser

import akka.stream.scaladsl.Source
import nanotest.TestSuite

object LineSplitterTest extends WithProcessor[List[String], Char, String] with TestSuite {
  val processor = () => new LineSplitter()
  val zero = Nil
  val fn = (xs: List[String], s: String) => xs ++ List(s)

  test("line splitter 1") {
    assert(process(Source("Hello\r\nWorld")) == "Hello" :: "World" :: Nil)
  }
}
