package controller.modules

import com.google.inject.{AbstractModule, Inject}
import model.Combat.CombatStrategy.DiceCombatStrategy
import model.Combat.CombatStrategyPort
import model.mapInit.MapInitPort
import model.mapInit.imp1.MapInit

object DefaultModule {

  given CombatStrategyPort = DiceCombatStrategy
  given MapInitPort = MapInit
  
}
