import TUI.*
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

    var currentPlayerIdx = 0

    while (players.exists(_.infantry > 0)) {
      val player = players(currentPlayerIdx)
      if(player.infantry > 0) {
        var validMove = false
        while (!validMove) {
          val (x, y, n) = ConsoleView.askForInfantryPlacement(player)
          controller.placeInfantry(player, x, y, n) match {
            case Right(mapData) =>
              ConsoleView.showTileMap(mapData)
              validMove = true
            case Left(msg) =>
              ConsoleView.showStatus(msg)
          }
        }
      }
      currentPlayerIdx = (currentPlayerIdx + 1) % players.length
    }
//    print(ConsoleView.welcome())
//    var MapData = MapInit.testMap_init()
//    val playersListObj = ConsoleView.start()
//    val playerList = playersListObj.toList
//
//    print(Map_Generation.print_map(MapData))
//    MapData  = ConsoleView.placeInfantry(
//      playerList,
//      2,
//      2,
//      MapData,
//      () => scala.io.StdIn.readInt(),
//      () => scala.io.StdIn.readInt(),
//      () => scala.io.StdIn.readInt())

  }
}