package Tests

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import controller.modules.DefaultModule.given
import controller.GameController.GameControllerPort
import model.Combat.impl.DiceCombatStrategy
import model.Combat.CombatStrategyPort
import model.mapInit.MapInitPort
import model.mapInit.impl.MapInit

class defaultModule_spec extends AnyWordSpec with Matchers {

  "DefaultModule" should {

    "provide a CombatStrategyPort given instance equal to DiceCombatStrategy" in {

      val strategy = summon[CombatStrategyPort]
      strategy shouldBe DiceCombatStrategy
    }

    "provide a MapInitPort given instance equal to MapInit" in {
      import controller.modules.DefaultModule.given

      val mapInit = summon[MapInitPort]
      mapInit shouldBe MapInit
    }
  }
}
