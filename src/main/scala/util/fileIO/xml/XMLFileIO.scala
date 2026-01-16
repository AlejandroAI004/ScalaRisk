package util.fileIO.xml

import controller.GameController.impl1.{GameState, PlayerState, TileState}
import model.GameEventS.GameStatePort
import model.GameEventS.states.PlacementState
import model.player.Player
import util.fileIO.FileIO
import util.gamePhase.GamePhase

import java.io.*
import scala.xml.*

class XMLFileIO extends FileIO {

  def gameStateToXml(gs: GameState): scala.xml.Elem = {
    <gameState phase={gs.phase.toString}>
      <currentPlayer>{gs.currentPlayerIndex}</currentPlayer>

      <players>
        {gs.players.map(p =>
          <player color={p.colorName} infantry={p.infantry.toString}/>
      )}
      </players>

      <map>
        {gs.mapData.map { row =>
        <row>
          {row.map { tile =>
            <tile
            parent={tile.parentName}
            player={tile.playerColor}
            soldiers={tile.soldiers.toString}
            />
        }}
        </row>
      }}
      </map>

    </gameState>
  }

  def gameStateFromXml(node: scala.xml.Node): GameState = {

    val phase =
      GamePhase.valueOf((node \ "@phase").text)

    val currentPlayerIndex =
      (node \ "currentPlayer").text.toInt

    val players: List[PlayerState] =
      (node \ "players" \ "player").map { pNode =>
        val color = (pNode \ "@color").text
        val infantry = (pNode \ "@infantry").text.toInt

        PlayerState(
          colorName = color,
          infantry = infantry,
          ownedTileNames = List.empty
        )
      }.toList

    val mapData =
      (node \ "map" \ "row").map { rowNode =>
        (rowNode \ "tile").map { tileNode =>
          TileState(
            parentName = (tileNode \ "@parent").text,
            playerColor = (tileNode \ "@player").text,
            soldiers = (tileNode \ "@soldiers").text.toInt
          )
        }.toList
      }.toList

    GameState(
      mapData = mapData,
      players = players,
      currentPlayerIndex = currentPlayerIndex,
      phase = phase,
      state = PlacementState
    )
  }

  override def save(gs: GameState): Unit = {
    XML.save("savegame.xml", gameStateToXml(gs))
  }

  override def load(): GameState = {
    val xml = XML.loadFile("savegame.xml")
    gameStateFromXml(xml)
  }
}