package model


enum direction:
  case north, south, west, east, northeast, northwest, southeast, southwest

case class Parent_Tile(neighbours: List[Parent_Tile] = List(), connections: List[direction] = List()) {
  def add_neighbour_tile(name: Parent_Tile): List[Parent_Tile] = {
    neighbours :+ name
  }
  def add_connection(dir: direction): List[direction] = {
    connections :+ dir
  }
}
def add_neighbour(target: Parent_Tile, neighbour: Parent_Tile, dir: direction): Parent_Tile = {
  Parent_Tile(
    target.add_neighbour_tile(neighbour),
    target.add_connection(dir))
}


  