package california.parser

import java.util.concurrent.atomic.{AtomicBoolean, AtomicReference}

import org.reactivestreams.{Processor, Subscriber, Subscription}

abstract class AbstractProcessor[I, O]() extends Processor[I, O] {

  private val subscriptionR: AtomicReference[Subscription] = new AtomicReference[Subscription]()
  private val subscriberR: AtomicReference[Subscriber[_ >: O]] = new AtomicReference[Subscriber[_ >: O]]()
  private val initialized = new AtomicBoolean()

  final def subscribe(s: Subscriber[_ >: O]): Unit = {
    subscriberR.compareAndSet(null, s)
    initialize()
  }

  final def onSubscribe(s: Subscription): Unit = {
    subscriptionR.compareAndSet(null, s)
    initialize()
  }

  protected def init(subscriber: Subscriber[_ >: O], subscription: Subscription): Unit

  private def initialize(): Unit =
    if (
      subscriberR.get() != null &&
        subscriptionR.get() != null &&
        !initialized.getAndSet(true)
    ) {
      init(subscriberR.get(), subscriptionR.get())
    }
}
