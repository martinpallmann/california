package california.parser

import java.util.concurrent.atomic.AtomicReference

import Unfolder._
import org.reactivestreams.{Subscriber, Subscription}

class Unfolder() extends AbstractProcessor[Char, Char] {

  private val state: AtomicReference[FoldState] = new AtomicReference(Normal)
  private var subscriber: Option[Subscriber[_ >: Char]] = None

  def onNext(t: Char): Unit = t match {
    case '\r' if state.compareAndSet(Normal, Cr)   =>
    case '\n' if state.compareAndSet(Cr, CrLf)     =>
    case ' '  if state.compareAndSet(CrLf, Normal) =>
    case '\t' if state.compareAndSet(CrLf, Normal) =>
    case x    if state.compareAndSet(Cr, Normal)   =>
      subscriber.foreach(_.onNext('\r'))
      subscriber.foreach(_.onNext(x))
    case x    if state.compareAndSet(CrLf, Normal) =>
      subscriber.foreach(_.onNext('\r'))
      subscriber.foreach(_.onNext('\n'))
      subscriber.foreach(_.onNext(x))
    case x                                         =>
      subscriber.foreach(_.onNext(x))
  }

  def onError(t: Throwable): Unit = subscriber.foreach(_.onError(t))

  def onComplete(): Unit = subscriber.foreach(_.onComplete())

  protected def init(s1: Subscriber[_ >: Char], s2: Subscription): Unit = {
    subscriber = Some(s1)
    s1.onSubscribe(s2)
  }
}

object Unfolder {
  private [Unfolder] sealed trait FoldState
  private [Unfolder] case object Normal extends FoldState
  private [Unfolder] case object Cr extends FoldState
  private [Unfolder] case object CrLf extends FoldState
}
