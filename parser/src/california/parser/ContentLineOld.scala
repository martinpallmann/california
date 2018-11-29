package california.parser

case class ContentLineOld(line: String) {
  import ContentLineOld._
  override def toString: String = fold(75)(line)
}

object ContentLineOld {
  val crlf = "\r\n"
  val fold: Int => String => String = cols => s =>
    (s.take(cols) :: s.drop(cols).grouped(cols - 1).toList)
      .mkString("", s"$crlf ", crlf)
}
