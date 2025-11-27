package model

import scala.util.Random

trait CombatStrategy {
  def resolveAttack(
                     attacker: Tile,
                     defender: Tile,
                     troops: Int
                   ): (Tile, Tile)
}

object DiceCombatStrategy extends CombatStrategy {

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
      // Angreifer gewinnt: verteidiger verliert alles,
      // Angreifer verliert anteilig, aber weniger, wenn er deutlich stärker war
      val lossFactor = 1.0 - winProb // je stärker, desto kleiner
      val attLoss = math.max(1, (attSoldiers * lossFactor).round.toInt)
      val survivors = attSoldiers - attLoss

      val newFrom = attacker.copy(soldiers = attacker.soldiers - attSoldiers)
      val newTo = Tile(defender.parent, attacker.player, survivors.max(1))

      (newFrom, newTo)
    } else {
      // Verteidiger hält: Angreifer verliert hauptsächlich seine Angriffstruppen,
      // Verteidiger verliert auch etwas, abhängig von Verhältnis
      val defLossFactor = winProb // je stärker Angreifer, desto mehr verliert Verteidiger auch
      val defLoss = math.max(0, (defSoldiers * defLossFactor).round.toInt)
      val newDefTroops = (defSoldiers - defLoss).max(1)

      val newFrom = attacker.copy(soldiers = attacker.soldiers - attSoldiers)
      val newTo = defender.copy(soldiers = newDefTroops)

      (newFrom, newTo)
    }
  }
}

 object SimpleCombatStrategy extends CombatStrategy {
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