package Tests

import controller.GameController.impl1.{GameState, PlayerState, TileState}
import model.GameEventS.states.PlacementState
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.{Format, JsString, Json}
import util.fileIO.json.JsonFileIO
import util.fileIO.xml.XMLFileIO
import util.gamePhase.GamePhase

import scala.xml.{Elem, XML}
import java.nio.file.{Files, Path, Paths}
import scala.util.Try

class jsonfileio_spec extends AnyWordSpec with Matchers {

  private def sampleGameState: GameState =
    GameState(
      mapData = List(
        List(
          TileState("A", "red", 3),
          TileState("B", "blue", 1)
        ),
        List(
          TileState("C", "red", 2),
          TileState("D", "blue", 5)
        )
      ),
      players = List(
        PlayerState("red", infantry = 10, ownedTileNames = List("A", "C")),
        PlayerState("blue", infantry = 8, ownedTileNames = List("B", "D"))
      ),
      currentPlayerIndex = 1,
      phase = GamePhase.Offense,
      state = PlacementState
    )

  private val savePath = Paths.get("savegame.json")

  private def cleanup(): Unit =
    Try(Files.deleteIfExists(savePath))

  "JsonFileIO.save" should {

    "write savegame.json to disk" in {
      cleanup()

      val io = new JsonFileIO
      val gs = sampleGameState

      io.save(gs)

      Files.exists(savePath) shouldBe true
      Files.size(savePath) should be > 0L

      cleanup()
    }
  }

  "JsonFileIO.load" should {

    "load a previously saved game state" in {
      cleanup()

      val io = new JsonFileIO
      val gs = sampleGameState

      io.save(gs)
      val loaded = io.load()

      loaded.phase shouldBe gs.phase
      loaded.currentPlayerIndex shouldBe gs.currentPlayerIndex

      loaded.players.map(p => (p.colorName, p.infantry)) shouldBe
        gs.players.map(p => (p.colorName, p.infantry))

      loaded.mapData shouldBe gs.mapData

      // Einschränkungen deiner Implementierung
      loaded.state shouldBe PlacementState

      cleanup()
    }
  }

  "JsonFileIO round-trip" should {

    "preserve all persisted fields across save and load" in {
      cleanup()

      val io = new JsonFileIO
      val gs = sampleGameState

      io.save(gs)
      val loaded = io.load()

      loaded.mapData shouldBe gs.mapData
      loaded.players shouldBe gs.players
      loaded.currentPlayerIndex shouldBe gs.currentPlayerIndex
      loaded.phase shouldBe gs.phase

      // bewusst immer PlacementState
      loaded.state shouldBe PlacementState

      cleanup()
    }
  }

  "JsonFileIO GamePhase JSON format" should {

    "serialize and deserialize GamePhase correctly" in {
      val io = new JsonFileIO
      implicit val fmt: Format[GamePhase] = io.gamePhaseFormat

      val phase = GamePhase.Offense
      val json = Json.toJson(phase)

      json shouldBe JsString("Offense")
      json.as[GamePhase] shouldBe GamePhase.Offense
    }
  }
}
