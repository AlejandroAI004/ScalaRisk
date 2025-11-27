package model

trait playerFactory {
  def colorName: String
  var infantry: Int
  override def toString: String = colorName
}