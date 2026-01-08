package model.tile

enum direction:
  case north, south, west, east, northeast, northwest, southeast, southwest

case class Parent_Tile(
                        neighbours: List[Parent_Tile] = List(),
                        connections: List[direction] = List(),
                        name: String = ""  
                      ) {
  def add_neighbour_tile(tile: Parent_Tile): Parent_Tile =
    this.copy(neighbours = neighbours :+ tile)

  def add_connection(dir: direction): Parent_Tile =
    this.copy(connections = connections :+ dir)
}

def add_neighbour(target: Parent_Tile, neighbour: Parent_Tile, dir: direction): Parent_Tile = {
  target
    .add_neighbour_tile(neighbour)
    .add_connection(dir)
}


  