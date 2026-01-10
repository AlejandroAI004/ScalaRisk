package controller.modules

import model.*
import controller.*
import controller.GameController.GameControllerPort
import controller.GameController.impl1.GameController
import model.Combat.CombatStrategyPort
import model.Combat.CombatStrategy.DiceCombatStrategy
import model.mapInit.MapInitPort
import model.mapInit.imp1.MapInit
import model.player.Player


object DefaultModule {

  given CombatStrategyPort = DiceCombatStrategy
  given MapInitPort = MapInit

  given GameControllerPort =
    new GameController(
      summon[MapInitPort].createInitialMap(),
      List(new Player("red"), new Player("blue")),
      summon[CombatStrategyPort]
    )
}
