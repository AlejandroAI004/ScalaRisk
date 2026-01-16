package model.player

import model.tile.Tile

class Player(val colorName: String):
  var infantry: Int = 20
  var ownedTiles: List[Tile] = List()
  override def toString: String = colorName

object Player {
  def apply(color: String): Player =
    color match {
      case "red" => new Player("red")
      case "blue" => new Player("blue")
      case "pink" => new Player("pink")
      case "green" => new Player("green")
      case _ => new Player("grey")
    }
}