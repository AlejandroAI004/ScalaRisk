import TUI.*
import TUI.ConsoleView.placeInfantryFunctional
import model.*
import controller.*

import scala.io.StdIn

object main {
  def main(args: Array[String]): Unit = {
    println(ConsoleView.welcome())
    val playersListObj = ConsoleView.start()
    val mapData = MapInit.testMap_init()
    val players = playersListObj.toList
    val controller = new GameController(mapData, players)
    print(Map_Generation.print_map(mapData))

    val newMap = placeInfantryFunctional(players, mapData, controller)
    ConsoleView.showTileMap(newMap)
  }
}