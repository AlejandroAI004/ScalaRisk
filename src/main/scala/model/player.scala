package model


 class player(val colorName: String) extends playerFactory:
  var infantry: Int = 20
  var ownedTiles: List[Tile] = List()
  override def toString: String = colorName

  object player {
  def apply(color: String): player = color match {
    case "red"   => new player("red")
    case "blue"  => new player("blue")
    case "pink"  => new player("pink")
    case "green" => new player("green")
    case _   => new player("red")  // Fallback
  }
  }

   override def create(colorName: String): player =
     new player(colorName)
