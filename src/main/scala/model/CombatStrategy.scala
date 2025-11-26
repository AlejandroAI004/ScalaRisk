package model

trait CombatStrategy {
  def resolveAttack(
                     attacker: Tile,
                     defender: Tile,
                     troops: Int
                   ): (Tile, Tile)
}
