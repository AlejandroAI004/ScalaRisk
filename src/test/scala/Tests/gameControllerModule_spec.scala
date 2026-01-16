package Tests

import controller.GameController.impl1.GameState
import controller.modules.GameControllerModule
import model.Combat.CombatStrategyPort
import model.GameEventS.states.PlacementState
import model.mapInit.MapInitPort
import model.player.Player
import model.tile.{Parent_Tile, Tile}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import util.fileIO.FileIO
import util.gamePhase.GamePhase

class gameControllerModule_spec extends AnyWordSpec with Matchers {

  // ---- Stubs/Fakes ----

  private object DummyCombatStrategy extends CombatStrategyPort {
    override def resolveAttack(from: Tile, to: Tile, n: Int): (Tile, Tile) = (from, to)
  }

  private object DummyFileIO extends FileIO {
    override def save(gameState: GameState): Unit = ()
    override def load(): GameState =
      GameState(Nil, Nil, 0, GamePhase.Placement, PlacementState)
  }

  private final class StubMapInit(map: List[List[Tile]]) extends MapInitPort {
    override def createInitialMap(): List[List[Tile]] = map
  }

  private def mkTile(name: String): Tile =
    Tile(
      parent = Parent_Tile(name = "a"),
      player = new Player("empty"))

  // ---- Tests ----

  "GameControllerModule" should {

    "use the map produced by mapInit.createInitialMap()" in {
      val expectedMap = List(List(mkTile("A"), mkTile("B")))
      val mapInit = new StubMapInit(expectedMap)

      val module = new GameControllerModule(
        mapInit = mapInit,
        combatStrategy = DummyCombatStrategy,
        fileIO = DummyFileIO
      )

      module.initialMap shouldBe expectedMap
      module.tiles shouldBe expectedMap // tiles ist dein Getter für mapData
    }

    "create exactly two default players: red and blue" in {
      val mapInit = new StubMapInit(List(List(mkTile("A"))))

      val module = new GameControllerModule(
        mapInit = mapInit,
        combatStrategy = DummyCombatStrategy,
        fileIO = DummyFileIO
      )

      module.players.map(_.colorName) shouldBe List("red", "blue")
      module.currentPlayerIndex shouldBe 0
      module.currentPlayer.colorName shouldBe "red"
    }

    "store the injected combatStrategy and fileIO" in {
      val mapInit = new StubMapInit(List(List(mkTile("A"))))

      val module = new GameControllerModule(
        mapInit = mapInit,
        combatStrategy = DummyCombatStrategy,
        fileIO = DummyFileIO
      )

      module.combatStrategy shouldBe DummyCombatStrategy
      module.fileIO shouldBe DummyFileIO
    }

    "start in Placement phase (inherited GameController behavior)" in {
      val mapInit = new StubMapInit(List(List(mkTile("A"))))

      val module = new GameControllerModule(
        mapInit = mapInit,
        combatStrategy = DummyCombatStrategy,
        fileIO = DummyFileIO
      )

      module.currentPhase shouldBe GamePhase.Placement
      module.currentStateName shouldBe PlacementState.name
    }
  }
}