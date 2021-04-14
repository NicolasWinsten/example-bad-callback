import org.scalatest.compatible.Assertion
import org.scalatest.matchers.should
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.{ExecutionContextExecutor, Promise}
import scala.scalajs.js.timers.setTimeout

class callbacktest extends AsyncWordSpec with should.Matchers {
  override implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  "A callback" should {
    // this test reports the Arithmetic Exception
    "not pass by method 1" in {
      val callbacker = new CallBackerByMethod
      val promise = Promise[Assertion]
      callbacker.onCall {
        val x = 10 / 0
        one => promise success (one should be (1))
      }
      promise.future
    }
  }

  "A callback" should {
    // this test does not report the Arithmetic Exception
    "not pass by method 2" in {
      val callbacker = new CallBackerByMethod
      val promise = Promise[Assertion]
      callbacker.onCall { one =>
        val x = 10 / 0
        promise success (one should be (1))
      }
      promise.future
    }
  }
}

class CallBackerByMethod {
  var callback: Int => Unit = _ => ()

  def onCall(cb: Int => Unit): Unit = callback = cb

  setTimeout(1000)(callback(1))
}
