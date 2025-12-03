import TUI.*
import TUI.ConsoleView.ConsoleOffenseTurn
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

    players.foreach { p =>
      ConsoleOffenseTurn.executeTurn(p, controller)
    }
    ConsoleView.showTileMap(controller.tiles)

  }
}