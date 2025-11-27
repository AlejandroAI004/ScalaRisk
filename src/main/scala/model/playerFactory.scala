package model

trait playerFactory {
  def create(colorName: String): player
}

object DefaultPlayerFactory extends playerFactory {
  override def create(colorName: String): player =
    new player(colorName) 
}