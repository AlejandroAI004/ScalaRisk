package util.command

import model.player.playerList

class PlayerConfigManager {
  private val manager = new UndoRedoManager[playerList](new playerList())

  def size: Int = manager.state.toList.size

  def list: playerList = manager.state

  def addPlayer(color: String): Unit =
    manager.save(manager.state.addPlayer(color))

  def undo(): Unit = manager.undo()

  def redo(): Unit = manager.redo()
}