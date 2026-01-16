package util.observer

trait Observer {
  def update(): Unit
}

trait Observable {
  private var subscribers: Vector[Observer] = Vector.empty
  def add(o: Observer): Unit = subscribers :+= o
  def remove(o: Observer): Unit = subscribers = subscribers.filterNot(_ == o)
  protected def notifyObservers(): Unit = subscribers.foreach(_.update())
}