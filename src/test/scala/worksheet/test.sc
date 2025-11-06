import com.sun.javafx.scene.traversal.Direction
import direction.{east, north, northeast, northwest, south, southeast, southwest, west}
import logic.Map_Generation.print_row
import logic.{Parent_Tile, Tile}
val x = 3
val y = 2
val text = "Blau 5 "
print{
  ( "\\     |      /" * x + "\n" +
    "  +--------+  " * x + "\n" +
    ("__| " + text + "|__") * x + "\n" +
    "  |        |  " * x + "\n" +
    "  +--------+  " * x + "\n" +
    "/     |      \\" * x +"\n") * y
}
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

var konstanz = Parent_Tile(List(), List())
var meersburg = Parent_Tile(List(konstanz), List(direction.south))
var friedrichshafen = Parent_Tile(List(meersburg), List(direction.west))
konstanz = add_neighbour(konstanz, meersburg, direction.north)
meersburg = add_neighbour(meersburg, friedrichshafen, direction.east)




var myArmy = new Army;
println(myArmy)
myArmy.infantry_to_artillery(1)
println(myArmy)

