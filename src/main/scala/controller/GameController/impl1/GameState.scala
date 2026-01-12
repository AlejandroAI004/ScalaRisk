package controller.GameController.impl1

import model.GameEventS.GameStatePort
import model.player.Player
import model.tile.Tile
import util.gamePhase.GamePhase

case class GameState(
                      mapData: List[List[TileState]],
                      players: List[PlayerState],
                      currentPlayerIndex: Int,
                      phase: GamePhase,
                      state: GameStatePort
                    )

case class PlayerState(
                        colorName: String,
                        infantry: Int,
                        ownedTileNames: List[String]
                      )

case class TileState(
                      parentName: String,
                      playerColor: String,
                      soldiers: Int
                    )