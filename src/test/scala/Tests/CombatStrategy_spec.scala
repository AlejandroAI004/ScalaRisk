package Tests
import model.*
import model.Combat.CombatStrategy.{DiceCombatStrategy, SimpleCombatStrategy}
import model.player.Player
import model.tile.{Parent_Tile, Tile}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class CombatStrategy_spec extends AnyWordSpec with Matchers {

  "SimpleCombatStrategy" should {

    "move exactly n soldiers to defender tile and subtract them from attacker" in {
      val parentA = Parent_Tile()
      val parentD = Parent_Tile()

      val attackerPlayer = new Player("red")
      val defenderPlayer = new Player("blue")

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

    "use win probability 0.5 when total soldiers is zero" in {
      val parentA = Parent_Tile()
      val parentD = Parent_Tile()

      val attackerPlayer = new Player("red")
      val defenderPlayer = new Player("blue")

      val attackerTile = Tile(parentA, attackerPlayer, soldiers = 0)
      val defenderTile = Tile(parentD, defenderPlayer, soldiers = 0)

      val n = 0

      var attackerWins = 0
      var defenderHolds = 0

      for (_ <- 1 to 200) {
        val (newFrom, newTo) =
          DiceCombatStrategy.resolveAttack(attackerTile, defenderTile, n)

        // from-tile invariant
        newFrom.soldiers shouldBe 0
        newFrom.player shouldBe attackerPlayer

        // to-tile invariant
        newTo.soldiers should be >= 1

        if (newTo.player eq attackerPlayer) attackerWins += 1
        if (newTo.player eq defenderPlayer) defenderHolds += 1
      }

      // Bei p = 0.5 sollten beide Seiten vorkommen
      attackerWins should be > 0
      defenderHolds should be > 0
    }

    "always reduce attacker soldiers on from-tile by exactly the sent troops" in {
      val parentA = Parent_Tile()
      val parentD = Parent_Tile()

      val attackerPlayer = new Player("red")
      val defenderPlayer = new Player("blue")

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

      val attackerPlayer = new Player("red")
      val defenderPlayer = new Player("blue")

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

      val attackerPlayer = new Player("red")
      val defenderPlayer = new Player("blue")

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
