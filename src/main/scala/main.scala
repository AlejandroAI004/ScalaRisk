import view.*
import view.ConsoleView.{ConsoleOffenseTurn, offense_phaseFunctional}
import model.*
import controller.*

object main {
  def main(args: Array[String]): Unit = {
    val mapData = MapInit.testMap_init()
    val players = List(new Player("red"), new Player("blue"))
    val controller = new GameController(mapData, players, DiceCombatStrategy)
    GUIView.init(controller)
    println(ConsoleView.welcome())
    ConsoleView.init(controller)
    val t = new Thread(() => {
      ConsoleView.start(controller)
      controller.handleEvent(PlaceInfantryEvent)
      controller.handleEvent(AttackEvent)
    })
    t.start()

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