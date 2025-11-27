package Tests
import model.*
import controller.*

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
class gameState_spec extends AnyWordSpec with Matchers {
  "PlacementState" should {

    "have name \"Placement\"" in {
      PlacementState.name shouldBe "Placement"
    }

    "stay in PlacementState on AttackEvent" in {
      val p1      = new player("red")
      val players = List(p1)
      val mapData = MapInit.testMap_init()
      val ctrl    = new GameController(mapData, players, SimpleCombatStrategy)

      val next = PlacementState.handle(ctrl, players, AttackEvent)

      next shouldBe PlacementState
    }

    "switch to OffenseState on PlaceInfantryEvent when all players have no infantry left" in {
      val p1      = new player("red");  p1.infantry = 0
      val p2      = new player("blue"); p2.infantry = 0
      val players = List(p1, p2)
      val mapData = MapInit.testMap_init()
      val ctrl    = new GameController(mapData, players, SimpleCombatStrategy)

      val next = PlacementState.handle(ctrl, players, PlaceInfantryEvent)

      next shouldBe OffenseState
    }
  }

  "OffenseState" should {

    "have name \"Offense\"" in {
      OffenseState.name shouldBe "Offense"
    }

    "stay in OffenseState on AttackEvent" in {
      val p1      = new player("red")
      val players = List(p1)
      val mapData = MapInit.testMap_init()
      val ctrl    = new GameController(mapData, players, SimpleCombatStrategy)

      val next = OffenseState.handle(ctrl, players, AttackEvent)

      next shouldBe OffenseState
    }

    "stay in OffenseState on PlaceInfantryEvent" in {
      val p1      = new player("red")
      val players = List(p1)
      val mapData = MapInit.testMap_init()
      val ctrl    = new GameController(mapData, players, SimpleCombatStrategy)

      val next = OffenseState.handle(ctrl, players, PlaceInfantryEvent)

      next shouldBe OffenseState
    }
  }
}
