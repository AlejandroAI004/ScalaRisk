package util.fileIO.json

import controller.GameController.impl1.{PlayerState, TileState}
import util.gamePhase.GamePhase

case class PersistedGameState(
                               mapData: List[List[TileState]],
                               players: List[PlayerState],
                               currentPlayerIndex: Int,
                               phase: GamePhase
                             )
