package nanotest

import com.eed3si9n.expecty.Expecty
import minitest.{ExecutionContext, Future}
import minitest.api.{AbstractTestSuite, DefaultExecutionContext, Properties, TestSpec, Void}

trait TestSuite extends AbstractTestSuite {

  val assert = Expecty.assert

  implicit class ErrorOps[E <: Throwable](e: E) {
    def withoutStackTrace: E = {
      e.setStackTrace(new Array[StackTraceElement](0))
      e
    }
  }

  def run[A](f: => A): Unit => Void = _ => try {
    f
    ()
  } catch {
    case e: AssertionError => throw   e.withoutStackTrace
  }

  def runF[A](f: => Future[A]): Unit => Future[Unit] = _ => f.map(_ => ()).transform(
    identity,
    {
      case e: AssertionError => e.withoutStackTrace
      case e => e
    }
  )

  def test[A](name: String)(f: => A): Unit =
    synchronized {
      if (isInitialized) throw initError()
      propertiesSeq = propertiesSeq :+ TestSpec.sync[Unit](name, run(f))
    }

  def testAsync[A](name: String)(f: => Future[A]): Unit =
    synchronized {
      if (isInitialized) throw initError()
      propertiesSeq = propertiesSeq :+ TestSpec.async[Unit](name, runF(f))
    }

  lazy val properties: Properties[_] =
    synchronized {
      if (!isInitialized) isInitialized = true
      Properties[Unit](() => (), _ => Void.UnitRef, () => (), () => (), propertiesSeq)
    }

  private[this] var propertiesSeq = Seq.empty[TestSpec[Unit, Unit]]
  private[this] var isInitialized = false
  private[this] implicit lazy val ec: ExecutionContext =
    DefaultExecutionContext

  private[this] def initError() =
    new AssertionError(
      "Cannot define new tests after SimpleTestSuite was initialized"
    )
}
