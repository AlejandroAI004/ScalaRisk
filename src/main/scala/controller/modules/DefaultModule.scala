package controller.modules

import model.*
import controller.*

object DefaultModule {
  given CombatStrategy = DiceCombatStrategy

  given GameControllerPort =
    new GameController(
      MapInit.testMap_init(),
      List(new Player("red"), new Player("blue")),
      summon[CombatStrategy]
    )
}
