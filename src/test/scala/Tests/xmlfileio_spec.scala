package Tests

import controller.GameController.impl1.{GameState, PlayerState, TileState}
import model.GameEventS.impl.PlacementState
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import util.fileIO.xml.XMLFileIO
import util.gamePhase.GamePhase

import scala.xml.{Elem, XML}
import java.nio.file.{Files, Path, Paths}

class xmlfileio_spec extends AnyWordSpec with Matchers {

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
        PlayerState("red", infantry = 10, ownedTileNames = List("A", "C")), // wird beim XML-Lesen verworfen
        PlayerState("blue", infantry = 8, ownedTileNames = List("B", "D")) // wird beim XML-Lesen verworfen
      ),
      currentPlayerIndex = 1,
      phase = GamePhase.Offense,
      state = PlacementState // state wird beim XML-Lesen immer PlacementState
    )

  "XMLFileIO.gameStateToXml" should {

    "write phase attribute, currentPlayer, players, and map tiles" in {
      val io = new XMLFileIO
      val gs = sampleGameState

      val xml: Elem = io.gameStateToXml(gs)

      (xml \ "@phase").text shouldBe "Offense"
      (xml \ "currentPlayer").text.trim shouldBe "1"

      val players = (xml \ "players" \ "player")
      players should have size 2
      (players.head \ "@color").text shouldBe "red"
      (players.head \ "@infantry").text shouldBe "10"
      (players(1) \ "@color").text shouldBe "blue"
      (players(1) \ "@infantry").text shouldBe "8"

      val rows = (xml \ "map" \ "row")
      rows should have size 2

      val firstRowTiles = (rows.head \ "tile")
      firstRowTiles should have size 2
      (firstRowTiles.head \ "@parent").text shouldBe "A"
      (firstRowTiles.head \ "@player").text shouldBe "red"
      (firstRowTiles.head \ "@soldiers").text shouldBe "3"
    }
  }

  "XMLFileIO.gameStateFromXml" should {

    "parse a GameState from XML (phase, currentPlayerIndex, players, mapData)" in {
      val io = new XMLFileIO
      val gs = sampleGameState
      val xml = io.gameStateToXml(gs)

      val parsed = io.gameStateFromXml(xml)

      parsed.phase shouldBe GamePhase.Offense
      parsed.currentPlayerIndex shouldBe 1

      parsed.players.map(_.colorName) shouldBe List("red", "blue")
      parsed.players.map(_.infantry) shouldBe List(10, 8)

      // ownedTileNames werden in gameStateFromXml immer List.empty
      parsed.players.foreach(_.ownedTileNames shouldBe empty)

      parsed.mapData.flatten.map(_.parentName) should contain allOf("A", "B", "C", "D")
      parsed.mapData(0)(0) shouldBe TileState("A", "red", 3)

      // state wird beim Einlesen immer PlacementState gesetzt
      parsed.state shouldBe PlacementState
    }
  }

  "XMLFileIO XML round-trip" should {

    "preserve phase/currentPlayerIndex/players(color+infantry)/mapData across toXml->fromXml" in {
      val io = new XMLFileIO
      val gs = sampleGameState

      val parsed = io.gameStateFromXml(io.gameStateToXml(gs))

      parsed.phase shouldBe gs.phase
      parsed.currentPlayerIndex shouldBe gs.currentPlayerIndex

      parsed.players.map(p => (p.colorName, p.infantry)) shouldBe
        gs.players.map(p => (p.colorName, p.infantry))

      parsed.mapData shouldBe gs.mapData

      // bewusst NICHT gleich:
      parsed.players.map(_.ownedTileNames) shouldBe List(List.empty, List.empty)
      parsed.state shouldBe PlacementState
    }
  }

  "XMLFileIO save/load" should {

    "save should create savegame.xml in the actual working directory" in {
      val io = new XMLFileIO
      val gs = sampleGameState

      // Lösche evtl. vorhandene Datei im echten working dir
      val path = Paths.get("savegame.xml")
      try Files.deleteIfExists(path) catch {
        case _: Throwable => ()
      }

      io.save(gs)

      Files.exists(path) shouldBe true
      Files.size(path) should be > 0L

      val loaded = io.load()
      loaded.mapData shouldBe gs.mapData

      // cleanup
      try Files.deleteIfExists(path) catch {
        case _: Throwable => ()
      }
    }
  }
}
