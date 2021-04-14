import org.scalatest.compatible.Assertion
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

import scala.concurrent.{ExecutionContextExecutor, Promise}
import scala.scalajs.js.timers.setTimeout

class CallBackerByConstructor(callback: Int => Unit) {
  setTimeout(1000)(callback(1))
}

class callbacktest extends AsyncFlatSpec with should.Matchers {
  override implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    // this test works fine
  "A working test callback" should "pass no problem" in {
    val promise = Promise[Assertion]()
    new CallBackerByConstructor(one => promise success (one should be (1)))
    promise.future
  }

    // this test sometimes terminates the whole process with JSEnvRPC$RunTerminatedException so no other tests run!
  "A failing callback" should "fail test" in {
    val promise = Promise[Assertion]()
    new CallBackerByConstructor(one => promise success (one should be (2)))
    promise.future
  }

    // this test fails like it should, reports the arithmetic exception AND
    // it does not terminate the process with JSEnvRPC$RunTerminatedException
  "this failing callback" should "report arithmetic exception 1" in {
    val promise = Promise[Assertion]()
    new CallBackerByConstructor ({
      val x = 10 / 0
      one => promise success (one should be (1))
    })
    promise.future
  }

    // this test does not report the arithmetic exception. it just terminates
    // the process with JSEnvRPC$RunTerminatedException.
    // you'll notice the difference between this test and the last is that
    // the arithmetic exception occurs within the callback function
  "this failing callback" should "report arithmetic exception 2" in {
    val promise = Promise[Assertion]()
    new CallBackerByConstructor ({ one =>
      val x = 10 / 0
      promise success (one should be (1))
    })
    promise.future
  }
}
