package model.Combat

import model.tile.Tile

trait CombatStrategyPort {
  def resolveAttack(
                     attacker: Tile,
                     defender: Tile,
                     troops: Int
                   ): (Tile, Tile)
}
