package Tests
import model.*

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class CombatStrategy_spec extends AnyWordSpec with Matchers {

  "SimpleCombatStrategy" should {

    "move exactly n soldiers to defender tile and subtract them from attacker" in {
      val parentA = Parent_Tile()
      val parentD = Parent_Tile()

      val attackerPlayer = new player("red")
      val defenderPlayer = new player("blue")

      val attackerTile = Tile(parentA, attackerPlayer, soldiers = 15)
      val defenderTile = Tile(parentD, defenderPlayer, soldiers = 5)

      val n = 7

      val (newFrom, newTo) =
        SimpleCombatStrategy.resolveAttack(attackerTile, defenderTile, n)

      newFrom.soldiers shouldBe 15 - n
      newFrom.player   shouldBe attackerPlayer
      newFrom.parent   shouldBe parentA

      newTo.soldiers   shouldBe n
      newTo.player     shouldBe attackerPlayer   // Besitzer wechselt
      newTo.parent     shouldBe parentD
    }
  }

  "DiceCombatStrategy" should {

    "always reduce attacker soldiers on from-tile by exactly the sent troops" in {
      val parentA = Parent_Tile()
      val parentD = Parent_Tile()

      val attackerPlayer = new player("red")
      val defenderPlayer = new player("blue")

      val attackerTile = Tile(parentA, attackerPlayer, soldiers = 20)
      val defenderTile = Tile(parentD, defenderPlayer, soldiers = 10)

      val n = 6

      for (_ <- 1 to 20) {
        val (newFrom, _) =
          DiceCombatStrategy.resolveAttack(attackerTile, defenderTile, n)

        newFrom.soldiers shouldBe 20 - n
        newFrom.player   shouldBe attackerPlayer
        newFrom.parent   shouldBe parentA
      }
    }

    "never produce zero or negative soldiers on the target tile" in {
      val parentA = Parent_Tile()
      val parentD = Parent_Tile()

      val attackerPlayer = new player("red")
      val defenderPlayer = new player("blue")

      val attackerTile = Tile(parentA, attackerPlayer, soldiers = 10)
      val defenderTile = Tile(parentD, defenderPlayer, soldiers = 10)

      val n = 5

      for (_ <- 1 to 50) {
        val (_, newTo) =
          DiceCombatStrategy.resolveAttack(attackerTile, defenderTile, n)

        newTo.soldiers should be >= 1
      }
    }

    "sometimes let attacker win and sometimes defender hold the tile" in {
      val parentA = Parent_Tile()
      val parentD = Parent_Tile()

      val attackerPlayer = new player("red")
      val defenderPlayer = new player("blue")

      val attackerTile = Tile(parentA, attackerPlayer, soldiers = 15)
      val defenderTile = Tile(parentD, defenderPlayer, soldiers = 15)

      val n = 10

      var attackerWins  = false
      var defenderHolds = false

      for (_ <- 1 to 100) {
        val (_, newTo) =
          DiceCombatStrategy.resolveAttack(attackerTile, defenderTile, n)

        if (newTo.player eq attackerPlayer) attackerWins  = true
        if (newTo.player eq defenderPlayer) defenderHolds = true
      }

      attackerWins  shouldBe true
      defenderHolds shouldBe true
    }
  }
}
