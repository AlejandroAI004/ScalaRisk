import TUI.*
import model.*
import controller.*

import scala.io.StdIn

object main {
  def main(args: Array[String]): Unit = {
    ConsoleView.welcome()
    val playersList = StartGameController.start(ConsoleView)
    
//    print(startgame.welcome())
//    if(StdIn.readLine() != "y") {
//      System.exit(0)
//    }
//    var MapData = MapInit.testMap_init()
//    val playersList = startgame.start(
//      () => scala.io.StdIn.readInt(),
//      () => scala.io.StdIn.readLine()
//    )
//    print(startgame.print_playersList(playersList))
//    print(Map_Generation.print_map(MapData))
//    MapData = placeInfantry(playersList,
//      2,
//      2,
//      MapData,
//      () => scala.io.StdIn.readInt(),
//      () => scala.io.StdIn.readInt(),
//      () => scala.io.StdIn.readInt()
//    )

  }
}