import view.*
import view.ConsoleView.ConsoleOffenseTurn
import model.*
import controller.*

object main {
  def main(args: Array[String]): Unit = {
    val mapData = MapInit.testMap_init()
    val players = List(new Player("red"), new Player("blue"))
    val controller = new GameController(mapData, players, SimpleCombatStrategy)
    ConsoleView.init(controller)
    GUIView.init(controller)
    val t = new Thread(new Runnable {
      override def run(): Unit = ConsoleView.start(controller)
    })
    t.start()


    // GUI starten (blockiert den Main-Thread, bis Fenster zu ist)
    GUIView.main(args)
  }
    
//    println(ConsoleView.welcome())
//    val playersListObj = ConsoleView.start()
//    val mapData = MapInit.testMap_init()
//    val players = playersListObj.toList
//
//    val controller = new GameController(mapData, players, DiceCombatStrategy)
//    ConsoleView.init(controller)
//
//    print(Map_Generation.print_map(mapData))
//
//    controller.handleEvent(PlaceInfantryEvent)
//    ConsoleView.showTileMap(controller.tiles)
//
//    players.foreach { p =>
//      ConsoleOffenseTurn.executeTurn(p, controller)
//    }
//    ConsoleView.showTileMap(controller.tiles)

  }