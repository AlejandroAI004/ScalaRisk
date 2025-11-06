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




case class Tile(parent: Parent_Tile, owner: String, soldiers: Int = 1) {
}
val kn = Tile(konstanz, "blue")
val mb = Tile(meersburg, "red")
val fh = Tile(friedrichshafen, "red", 4)

def print_upper_conn(tiles: List[Tile]): String = {
  var res = ""
  for(i <- tiles) {
    if(i.parent.connections.contains(direction.northwest)) {res += "\\     "}
    else {res += "      "}
    if (i.parent.connections.contains(direction.north)) {res += "|"}
    else {res += " "}
    if (i.parent.connections.contains(direction.northeast)) {res += "      /"}
    else {res += "       "}
  }
  res + "\n"
}

def print_lower_conn(tiles: List[Tile]): String = {
  var res = ""
  for(i <- tiles) {
    if(i.parent.connections.contains(direction.southwest)) {res += "/     "}
    else {res += "      "}
    if (i.parent.connections.contains(direction.south)) {res += "|"}
    else {res += " "}
    if (i.parent.connections.contains(direction.southeast)) {res += "      \\"}
    else {res += "       "}
  }
  res + "\n"
}

def print_horizontal(tiles: List[Tile]): String = {
  var res = ""
  for(i <- tiles) {
    res += "  +--------+  "
  }
  res + "\n"
}

def print_upper_area(tiles: List[Tile]): String = {
  var res = ""
  for(i <- tiles) {
    if(i.parent.connections.contains(direction.west)) {res += "__| "}
    else {res += "  | "}
    res += i.owner + " " + i.soldiers + " " * (6 - i.owner.length - i.soldiers.toString.length)
    if (i.parent.connections.contains(direction.east)) {res += "|__"}
    else {res += "|  "}
  }
  res + "\n"
}

def print_lower_area(tiles: List[Tile]): String = {
  var res = ""
  for(i <- tiles) {
    res += "  |        |  "
  }
  res + "\n"
}

def print_row(tiles: List[Tile]): String = {
  print_upper_conn(tiles) +
  print_horizontal(tiles) +
  print_upper_area(tiles) +
  print_lower_area(tiles) +
  print_horizontal(tiles) +
  print_lower_conn(tiles)
}

print_row(List(mb,fh))
print_row(List(kn))

val emptyParent = Parent_Tile()
val owner = "blue"
val tile = Tile(emptyParent, owner)

print_row(List(tile))