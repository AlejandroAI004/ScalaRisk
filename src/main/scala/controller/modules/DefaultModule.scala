package controller.modules

import com.google.inject.{AbstractModule, Inject}
import model.Combat.impl.DiceCombatStrategy
import model.Combat.CombatStrategyPort
import model.mapInit.MapInitPort
import model.mapInit.impl.MapInit

object DefaultModule {

  given CombatStrategyPort = DiceCombatStrategy
  given MapInitPort = MapInit
  
}
