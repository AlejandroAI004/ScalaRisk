import TUI.*
import model.*
import controller.*

object main {
  def main(args: Array[String]): Unit = {
    println(ConsoleView.welcome())
    val playersListObj = ConsoleView.start()
    val mapData = MapInit.testMap_init()
    val players = playersListObj.toList

    val controller = new GameController(mapData, players, DiceCombatStrategy)
    ConsoleView.init(controller)

    print(Map_Generation.print_map(mapData))

    controller.handleEvent(PlaceInfantryEvent)
    ConsoleView.showTileMap(controller.tiles)

    controller.handleEvent(AttackEvent)
    ConsoleView.showTileMap(controller.tiles)

    //    val mapPlacement = placeInfantryFunctional(players, controller)
//    val mapOffense = offense_phaseFunctional(players, controller)
//    ConsoleView.showTileMap(mapOffense)

  }
}