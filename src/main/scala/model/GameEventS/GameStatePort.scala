package model.GameEventS

import controller.GameController.GameControllerPort
import model.player.Player

trait GameStatePort {
  def name: String
  def handle(controller: GameControllerPort, players: List[Player], e: GameEvent): GameStatePort
}
