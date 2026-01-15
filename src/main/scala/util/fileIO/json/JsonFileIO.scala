package util.fileIO.json

import controller.GameController.impl1.{GameState, PlayerState, TileState}
import model.GameEventS.states.PlacementState
import util.fileIO.FileIO
import util.gamePhase.GamePhase
import play.api.libs.json.{util, *}

import java.io.*
import scala.io.Source
import scala.util.Using

class JsonFileIO extends FileIO {

  implicit val tileStateFormat: OFormat[TileState] = Json.format[TileState]
  implicit val playerStateFormat: OFormat[PlayerState] = Json.format[PlayerState]
  implicit val gamePhaseFormat: Format[GamePhase] = {
    Format(
      Reads {
        case JsString(s) =>
          JsSuccess(GamePhase.valueOf(s))
        case _ =>
          JsError("GamePhase must be a string")
      },
      Writes(phase => JsString(phase.toString))
    )
  }
  implicit val pgsFormat: OFormat[PersistedGameState] = Json.format[PersistedGameState]

  override def save(gs: GameState): Unit = {
    val pgs = PersistedGameState(gs.mapData, gs.players, gs.currentPlayerIndex, gs.phase)
    val json = Json.toJson(pgs)
    val pw = new PrintWriter(new File("savegame.json"))
    try pw.write(json.toString()) finally pw.close()
  }

  override def load(): GameState = {
    val jsonString = Using.resource(Source.fromFile("savegame.json")) { source =>
      source.mkString
    }

    val pgs = Json.parse(jsonString).as[PersistedGameState]

    GameState(
      mapData = pgs.mapData,
      players = pgs.players,
      currentPlayerIndex = pgs.currentPlayerIndex,
      phase = pgs.phase,
      state = PlacementState
    )
  }
}
