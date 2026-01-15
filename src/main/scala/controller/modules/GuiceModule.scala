package controller.modules

import com.google.inject.AbstractModule
import controller.GameController.GameControllerPort
import model.Combat.CombatStrategyPort
import model.mapInit.MapInitPort
import DefaultModule.given
import util.fileIO.FileIO
import util.fileIO.json.JsonFileIO
import util.fileIO.xml.XMLFileIO

class GuiceModule extends AbstractModule {

  override def configure(): Unit = {

    bind(classOf[CombatStrategyPort])
      .toInstance(summon[CombatStrategyPort])

    bind(classOf[MapInitPort])
      .toInstance(summon[MapInitPort])

    bind(classOf[GameControllerPort])
      .to(classOf[GameControllerModule])

    bind(classOf[FileIO])
      .to(classOf[JsonFileIO])

  }
}
