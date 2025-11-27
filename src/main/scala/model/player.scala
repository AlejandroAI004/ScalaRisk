package model


 class player(val colorName: String) extends playerFactory:
  var infantry: Int = 20
  var ownedTiles: List[Tile] = List()
  override def toString: String = colorName

   override def create(colorName: String): player = new player(colorName) 
