package model.Combat.CombatStrategy
import model.Combat

import model.Combat.CombatStrategyPort
import model.tile.Tile

object SimpleCombatStrategy extends CombatStrategyPort {
  override def resolveAttack(
                              attacker: Tile,
                              defender: Tile,
                              troops: Int
                            ): (Tile, Tile) = {
    val newFrom = attacker.copy(soldiers = attacker.soldiers - troops)
    val newTo = Tile(defender.parent, attacker.player, troops)
    (newFrom, newTo)
  }
}