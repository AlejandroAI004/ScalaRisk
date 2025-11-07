import TUI.*
import logic.*

import scala.io.StdIn

object main {
  def main(args: Array[String]): Unit = {
    print(startgame.welcome())
    if(StdIn.readLine() != "y") {
      System.exit(0)
    }
    var MapData = MapInit.testMap_init()
    val playersList = startgame.start()
    print(startgame.print_playersList(playersList))
    print(Map_Generation.print_map(MapData))
    for p <- playersList do {
      MapData = placeInfantry(p, 2, 2, MapData)
    }
  }
}