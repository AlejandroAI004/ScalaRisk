package model

trait Command {
  def doStep(): Unit
  def undoStep(): Unit
  def redoStep(): Unit
}

class PlayerConfigManager {
  private var current: playerList = new playerList()
  private var undoStack: List[playerList] = Nil
  private var redoStack: List[playerList] = Nil

  def list: playerList = current
  def size: Int = current.toList.size

  def addPlayer(color: String): Unit = {
    undoStack = current :: undoStack
    redoStack = Nil
    current = current.addPlayer(color)
  }

  def undo(): Unit = undoStack match {
    case head :: tail =>
      redoStack = current :: redoStack
      current = head
      undoStack = tail
    case Nil => ()
  }

  def redo(): Unit = redoStack match {
    case head :: tail =>
      undoStack = current :: undoStack
      current = head
      redoStack = tail
    case Nil => ()
  }
}
