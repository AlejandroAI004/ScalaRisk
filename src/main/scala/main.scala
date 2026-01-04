import view.*
import view.ConsoleView.{ConsoleOffenseTurn, offense_phaseFunctional}
import model.*
import controller.*
import controller.modules.DefaultModule.given 

object main {
  @main def run(): Unit = {
    val controller: GameControllerPort = summon[GameControllerPort]

    GUIView.init(controller)
    println(ConsoleView.welcome())
    ConsoleView.init(controller)

    val t = new Thread(() => {
      ConsoleView.start(controller)
      controller.handleEvent(PlaceInfantryEvent)
      controller.handleEvent(AttackEvent)
    })
    t.start()

    GUIView.main(Array.empty)
  }
}