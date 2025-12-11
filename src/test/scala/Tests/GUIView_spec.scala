package Tests

import model.*
import controller.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import view.GUIView
import model.Player
import scalafx.scene.paint.Color

class GUIView_spec extends AnyWordSpec with Matchers {

  // einfacher Dummy-Controller, nur für init/update-Tests
  class DummyController extends GameController(
    MapInit.testMap_init(), // oder ein kleines Test-Map
    Nil,
    DiceCombatStrategy
  ) {
    var addedObservers: List[Observer] = Nil
    override def add(o: Observer): Unit = {
      addedObservers = o :: addedObservers
      super.add(o)
    }
  }

  "GUIView.init" should {
    "register itself as observer in the controller" in {
      val ctrl = new DummyController

      GUIView.init(ctrl)

      ctrl.addedObservers should contain (GUIView)
    }
  }

  "GUIView.colorForPlayer" should {
    "map known colorNames to the correct Color" in {
      val redPlayer   = new Player("red")
      val bluePlayer  = new Player("blue")
      val pinkPlayer  = new Player("pink")
      val greenPlayer = new Player("green")
      val otherPlayer = new Player("yellow")

      GUIView.colorForPlayer(redPlayer)   shouldBe Color.Red
      GUIView.colorForPlayer(bluePlayer)  shouldBe Color.Blue
      GUIView.colorForPlayer(pinkPlayer)  shouldBe Color.HotPink
      GUIView.colorForPlayer(greenPlayer) shouldBe Color.Green
      GUIView.colorForPlayer(otherPlayer) shouldBe Color.Gray
    }
  }

  "GUIView.update" should {
    "not crash and access remainingInfantryPerPlayer" in {
      val ctrl = new DummyController
      GUIView.init(ctrl)

      // keine Exception erwartet
      noException should be thrownBy {
        GUIView.update()
      }
    }
  }
}