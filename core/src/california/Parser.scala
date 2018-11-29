package california

object Parser {
  def parse(s: String): Either[String, List[Calendar]] = ???
}

class Parser(col: Int, row: Int, buffer: String)
