import view.*
import view.ConsoleView.{ConsoleOffenseTurn, offense_phaseFunctional}
import model.*
import controller.*

object main {
  def main(args: Array[String]): Unit = {
//    println(ConsoleView.welcome())
//
//    val controller = new GameController(List(List()), Nil, DiceCombatStrategy)
//    ConsoleView.init(controller)
//
//    val playersListObj = ConsoleView.start(controller)
//
//    print(Map_Generation.print_map(controller.tiles))

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
    


  }