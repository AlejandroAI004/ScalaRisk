package Tests
import model.*
import util.observer.{Observable, Observer}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class observer_spec extends AnyWordSpec with Matchers {

  class TestObservable extends Observable {
    def triggerNotify(): Unit = notifyObservers()
  }

  class TestObserver extends Observer {
    var calls: Int = 0
    override def update(): Unit = calls += 1
  }

  "Observable" should {

    "notify all subscribed observers" in {
      val observable = new TestObservable
      val o1 = new TestObserver
      val o2 = new TestObserver

      observable.add(o1)
      observable.add(o2)

      observable.triggerNotify()

      o1.calls shouldBe 1
      o2.calls shouldBe 1
    }

    "not notify removed observers" in {
      val observable = new TestObservable
      val o1 = new TestObserver
      val o2 = new TestObserver

      observable.add(o1)
      observable.add(o2)
      observable.remove(o1)

      observable.triggerNotify()

      o1.calls shouldBe 0
      o2.calls shouldBe 1
    }

    "support multiple notifications" in {
      val observable = new TestObservable
      val o = new TestObserver

      observable.add(o)

      observable.triggerNotify()
      observable.triggerNotify()

      o.calls shouldBe 2
    }
  }
}