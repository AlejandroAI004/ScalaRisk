package model

trait Command {
  def doStep(): Unit
  def undoStep(): Unit
  def redoStep(): Unit
}

class UndoManager {
  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil

  def doStep(cmd: Command): Unit = {
    undoStack = cmd :: undoStack
    redoStack = Nil
    cmd.doStep()
  }

  def undoStep(): Unit = undoStack match {
    case Nil => ()
    case head :: tail =>
      head.undoStep()
      undoStack = tail
      redoStack = head :: redoStack
  }

  def redoStep(): Unit = redoStack match {
    case Nil => ()
    case head :: tail =>
      head.redoStep()
      redoStack = tail
      undoStack = head :: undoStack
  }
}
