package util.fileIO

import controller.GameController.impl1.GameState

trait FileIO {
  def save(gameState: GameState): Unit
  def load(): GameState
}