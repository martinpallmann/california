package california.parser

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Source}
import org.reactivestreams.Processor

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

trait WithProcessor[U, I, O] {

  def processor: () => Processor[I, O]
  def zero: U
  def fn: (U, O) => U

  implicit val system: ActorSystem = ActorSystem("Test")
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit class FutureOps[A](f: Future[A]) {
    def sync: A = Await.result(f, 1.second)
  }

  def process(source: Source[I, NotUsed]): U =
    source
      .via(Flow.fromProcessor(processor))
      .runFold(zero)(fn)
      .sync

}
