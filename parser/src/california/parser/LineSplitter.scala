package california.parser

import java.util.concurrent.atomic.{AtomicBoolean, AtomicLong, AtomicReference}
import org.reactivestreams.{Subscriber, Subscription}

class LineSplitter() extends AbstractProcessor[Char, String] {

  import LineSplitter._

  private val buffer = new StringBuilder(75, "")
  private val state = new AtomicReference[SplitState](Normal)
  private var downstream: Subscriber[_ >: String] = _
  private var upstream: Subscription = _
  private val demand = new AtomicLong(0L)
  private val completed = new AtomicBoolean(false)
  private val subscription = new Subscription {
    def request(n: Long): Unit =
      demand.updateAndGet(_ + n)
      next()
    def cancel(): Unit =
      demand.set(0L)
  }

  override protected def init(s1: Subscriber[_ >: String], s2: Subscription): Unit = {
    downstream = s1
    upstream = s2
    downstream.onSubscribe(subscription)
    upstream.request(75)
  }

  private def next(): Unit = if (buffer.nonEmpty && demand.get() > 0) {
    downstream.onNext(buffer.toString())
    buffer.delete(0, buffer.length)
    if (completed.get()) downstream.onComplete()
  }

  def onNext(t: Char): Unit = t match {
    case '\r' if state.compareAndSet(Normal, Cr) =>
    case '\n' if state.compareAndSet(Cr, Normal) =>
      next()
    case x if state.compareAndSet(Cr, Normal) =>
      buffer.append(s"\r$x")
    case x =>
      buffer.append(x)
  }

  def onError(t: Throwable): Unit = downstream.onError(t)

  def onComplete(): Unit = {
    completed.set(true)
    next()
  }
}

object LineSplitter {
  private [LineSplitter] sealed trait SplitState
  private [LineSplitter] case object Normal extends SplitState
  private [LineSplitter] case object Cr extends SplitState
}
