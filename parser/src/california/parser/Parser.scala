package california.parser

import atto._
import Atto._
import Parser.ParserState
import org.reactivestreams.{Subscriber, Subscription}

class Parser extends Subscriber[Char] {
  var state: ParserState = new ParserState("")
  def onSubscribe(s: Subscription): Unit = s.request(10)
  def onNext(t: Char): Unit = state = state + t
  def onError(t: Throwable): Unit = ???
  def onComplete(): Unit = ???
}

object Parser {

  sealed trait FoldState
  case object Normal extends FoldState
  case object Cr extends FoldState
  case object CrLf extends FoldState

  class ParserState(value: String, foldState: FoldState = Normal) {
    def + (c: Char): ParserState = (foldState, c) match {
      case (Normal, '\r') => new ParserState(value, Cr)
      case (Normal,  x  ) => new ParserState(value + x)
      case (Cr,     '\n') => new ParserState(value, CrLf)
      case (Cr,      x  ) => new ParserState(value + s"\r$x", Normal)
      case (CrLf,   ' ' ) => new ParserState(value, Normal)
      case (CrLf,   '\t') => new ParserState(value, Normal)
      case (CrLf,    x  ) => new ParserState(value + s"\r\n$x", Normal)
    }
  }
//  val contentLine: atto.Parser[ContentLine] = many(letter).
}
