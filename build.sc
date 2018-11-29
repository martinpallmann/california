import mill._
import scalalib._

trait Minitest extends TestModule {
  def testFrameworks = Seq("minitest.runner.Framework")
  def moduleDeps = super.moduleDeps ++ Seq(nanotest)
}

object nanotest extends ScalaModule {
  def scalaVersion = "2.12.7"
  def ivyDeps = Agg(
    ivy"io.monix::minitest:2.2.2",
    ivy"com.eed3si9n.expecty::expecty:0.11.0"
  )
}

object core extends ScalaModule {
  def scalaVersion = "2.12.7"
  object test extends Tests with Minitest 
}

object parser extends ScalaModule {
  def scalaVersion = "2.12.7"
  object test extends Tests with Minitest {
    def ivyDeps = Agg(
      ivy"com.typesafe.akka::akka-stream:2.5.18"
    )
  }
  def ivyDeps = Agg(
    ivy"org.tpolecat::atto-core:0.6.4",
    ivy"org.reactivestreams:reactive-streams:1.0.2"
  )
}

