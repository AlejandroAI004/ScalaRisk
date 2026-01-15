package controller.modules

import com.google.inject.Inject
import controller.GameController.impl1.GameController
import model.Combat.CombatStrategyPort
import model.mapInit.MapInitPort
import model.player.Player
import util.fileIO.FileIO


class GameControllerModule @Inject() (
                                       mapInit: MapInitPort,
                                       combatStrategy: CombatStrategyPort,
                                       fileIO: FileIO
                                     ) extends GameController(
  mapInit.createInitialMap(),
  List(new Player("red"), new Player("blue")),
  combatStrategy,
  fileIO
)
