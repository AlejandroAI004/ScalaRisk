package util.command

class UndoRedoManager[T](initial: T) {

  private var current: T = initial
  private var undoStack: List[T] = Nil
  private var redoStack: List[T] = Nil

  def state: T = current

  def save(newState: T): Unit = {
    undoStack = current :: undoStack
    redoStack = Nil
    current = newState
  }

  def undo(): Option[T] = undoStack match {
    case head :: tail =>
      redoStack = current :: redoStack
      current = head
      undoStack = tail
      Some(current)
    case Nil => None
  }

  def redo(): Option[T] = redoStack match {
    case head :: tail =>
      undoStack = current :: undoStack
      current = head
      redoStack = tail
      Some(current)
    case Nil => None
  }
}
