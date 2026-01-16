package model.Combat.CombatStrategy

import model.Combat
import model.Combat.CombatStrategyPort
import model.tile.Tile

import scala.util.Random

object DiceCombatStrategy extends CombatStrategyPort {

  private val rnd = new Random()

  override def resolveAttack(
                              attacker: Tile,
                              defender: Tile,
                              soldiers: Int
                            ): (Tile, Tile) = {
    val attSoldiers = soldiers
    val defSoldiers = defender.soldiers

    val total = attSoldiers + defSoldiers
    val winProb: Double =
      if (total == 0) 0.5
      else attSoldiers.toDouble / total.toDouble

    val attackerWins = rnd.nextDouble() < winProb

    if (attackerWins) {
      val lossFactor = 1.0 - winProb
      val attLoss = math.max(1, (attSoldiers * lossFactor).round.toInt)
      val survivors = attSoldiers - attLoss

      val newFrom = attacker.copy(soldiers = attacker.soldiers - attSoldiers)
      val newTo = Tile(defender.parent, attacker.player, survivors.max(1))

      (newFrom, newTo)
    } else {
      val defLossFactor = winProb
      val defLoss = math.max(0, (defSoldiers * defLossFactor).round.toInt)
      val newDefTroops = (defSoldiers - defLoss).max(1)

      val newFrom = attacker.copy(soldiers = attacker.soldiers - attSoldiers)
      val newTo = defender.copy(soldiers = newDefTroops)

      (newFrom, newTo)
    }
  }
}