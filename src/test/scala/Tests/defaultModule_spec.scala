package Tests

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import controller.modules.DefaultModule.given
import controller.GameController.GameControllerPort
import model.Combat.CombatStrategyPort
import model.mapInit.MapInitPort

class defaultModule_spec extends AnyWordSpec with Matchers {

  "DefaultModule" should {

    "provide givens for strategy, mapInit and controller" in {
      val strategy = summon[CombatStrategyPort]
      val mapInit  = summon[MapInitPort]
      val ctrl     = summon[GameControllerPort]

      strategy should not be null
      mapInit should not be null
      ctrl should not be null
    }

    "wire GameController with an initialized map and players" in {
      val ctrl = summon[GameControllerPort]

      ctrl.tiles.nonEmpty shouldBe true
      ctrl.allPlayers.map(_.colorName) should contain allOf ("red", "blue")
    }
  }
}
