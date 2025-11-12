package logic

import TUI.*

 class player(val colorName: String):
  var infantry: Int = 20
  override def toString: String = colorName
  var ownedTiles: List[Tile] = List()
