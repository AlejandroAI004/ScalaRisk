package Tests

import model.*
import controller.*
import controller.GameController.impl1.{GameController, GameState}
import model.Combat.impl.SimpleCombatStrategy
import model.GameEventS.impl.{OffenseState, PlacementState}
import model.GameEventS.{AttackEvent, PlaceInfantryEvent}
import model.mapInit.impl
import model.mapInit.impl.MapInit
import model.player.Player
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import util.fileIO.FileIO
import util.gamePhase.GamePhase
class gameState_spec extends AnyWordSpec with Matchers {
  
  object TestFileIO extends FileIO {

    override def save(gameState: GameState): Unit = () // no-op

    override def load(): GameState =
      GameState(
        mapData = Nil,
        players = Nil,
        currentPlayerIndex = 0,
        phase = GamePhase.Placement,
        state = PlacementState
      )
  }
  
  "PlacementState" should {

    "have name \"Placement\"" in {
      PlacementState.name shouldBe "Placement"
    }

    "stay in PlacementState on AttackEvent" in {
      val p1      = new Player("red")
      val players = List(p1)
      val mapData = MapInit.createInitialMap()
      val ctrl    = new GameController(mapData, players, SimpleCombatStrategy,TestFileIO)

      val next = PlacementState.handle(ctrl, players, AttackEvent)

      next shouldBe PlacementState
    }

    "switch to OffenseState on PlaceInfantryEvent when all players have no infantry left" in {
      val p1      = new Player("red");  p1.infantry = 0
      val p2      = new Player("blue"); p2.infantry = 0
      val players = List(p1, p2)
      val mapData = impl.MapInit.createInitialMap()
      val ctrl    = new GameController(mapData, players, SimpleCombatStrategy,TestFileIO)

      val next = PlacementState.handle(ctrl, players, PlaceInfantryEvent)

      next shouldBe OffenseState
    }
  }

  "OffenseState" should {

    "have name \"Offense\"" in {
      OffenseState.name shouldBe "Offense"
    }

    "stay in OffenseState on AttackEvent" in {
      val p1      = new Player("red")
      val players = List(p1)
      val mapData = impl.MapInit.createInitialMap()
      val ctrl    = new GameController(mapData, players, SimpleCombatStrategy,TestFileIO)

      val next = OffenseState.handle(ctrl, players, AttackEvent)

      next shouldBe OffenseState
    }

    "stay in OffenseState on PlaceInfantryEvent" in {
      val p1      = new Player("red")
      val players = List(p1)
      val mapData = impl.MapInit.createInitialMap()
      val ctrl    = new GameController(mapData, players, SimpleCombatStrategy,TestFileIO)

      val next = OffenseState.handle(ctrl, players, PlaceInfantryEvent)

      next shouldBe OffenseState
    }
  }
}
