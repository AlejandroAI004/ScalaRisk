import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions._
import view.*
import controller.GameController.GameControllerPort
import controller.modules.GuiceModule
import model.GameEventS.{AttackEvent, PlaceInfantryEvent} 

object main {
  @main def run(): Unit = {

    val injector = Guice.createInjector(new GuiceModule)
    val controller: GameControllerPort = injector.instance[GameControllerPort]

    GUIView.init(controller)
    println(ConsoleView.welcome())
    ConsoleView.init(controller)

    val t = new Thread(() => {
      ConsoleView.start(controller)
      controller.handleEvent(PlaceInfantryEvent)
      controller.handleEvent(AttackEvent)
    })
    t.setDaemon(true)
    t.start()

    GUIView.main(Array.empty)
  }
}