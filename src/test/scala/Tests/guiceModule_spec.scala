package Tests

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import util.fileIO.json.JsonFileIO

import com.google.inject.Guice
import controller.GameController.GameControllerPort
import controller.modules.{GameControllerModule, GuiceModule}
import model.Combat.CombatStrategyPort
import model.mapInit.MapInitPort
import util.fileIO.FileIO

class guiceModule_spec extends AnyWordSpec with Matchers {

  "GuiceModule" should {

    "bind FileIO to JsonFileIO" in {
      val injector = Guice.createInjector(new GuiceModule)

      val fileIO = injector.getInstance(classOf[FileIO])
      fileIO shouldBe a[JsonFileIO]
    }

    "bind GameControllerPort to GameControllerModule" in {
      val injector = Guice.createInjector(new GuiceModule)

      val controller = injector.getInstance(classOf[GameControllerPort])
      controller shouldBe a[GameControllerModule]
    }

    "inject JsonFileIO into GameControllerModule via FileIO binding" in {
      val injector = Guice.createInjector(new GuiceModule)

      val controller = injector
        .getInstance(classOf[GameControllerPort])
        .asInstanceOf[GameControllerModule]

      controller.fileIO shouldBe a[JsonFileIO]
    }

    "return the same instance for CombatStrategyPort and MapInitPort (toInstance bindings)" in {
      val injector = Guice.createInjector(new GuiceModule)

      val cs1 = injector.getInstance(classOf[CombatStrategyPort])
      val cs2 = injector.getInstance(classOf[CombatStrategyPort])
      cs1 shouldBe theSameInstanceAs (cs2)

      val mi1 = injector.getInstance(classOf[MapInitPort])
      val mi2 = injector.getInstance(classOf[MapInitPort])
      mi1 shouldBe theSameInstanceAs (mi2)
    }

    "return different instances for FileIO (unscoped class binding)" in {
      val injector = Guice.createInjector(new GuiceModule)

      val f1 = injector.getInstance(classOf[FileIO])
      val f2 = injector.getInstance(classOf[FileIO])

      // Guice default: unscoped = new instance each time
      f1 should not be theSameInstanceAs (f2)
    }
  }
}