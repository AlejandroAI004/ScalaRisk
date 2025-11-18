import TUI.*
import TUI.ConsoleView.{offense_phaseFunctional, placeInfantryFunctional}
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

    val mapPlacement = placeInfantryFunctional(players, mapData, controller)
    ConsoleView.showTileMap(mapPlacement)
    val mapOffense = offense_phaseFunctional(players, mapPlacement, controller)
    ConsoleView.showTileMap(mapOffense)

  }
}