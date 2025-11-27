package Tests
import model.*
import controller.*

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
class gameState_spec extends AnyWordSpec with Matchers {
  "PlacementState" should {

    "switch to OffenseState when all players have no infantry left" in {
      val p1 = new player("red")
      p1.infantry = 0
      val p2 = new player("blue")
      p2.infantry = 0
      val players = List(p1, p2)

      val mapData = MapInit.testMap_init()
      val controller = new GameController(mapData, players, SimpleCombatStrategy)

      val next = PlacementState.handle(controller, players, PlaceInfantryEvent)

      next shouldBe OffenseState
    }
  }
}
