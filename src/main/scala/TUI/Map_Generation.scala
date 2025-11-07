package TUI

object Map_Generation {
  def print_upper_conn(tiles: List[Tile]): String = {
    var res = ""
    for (i <- tiles) {
      if (i.parent.connections.contains(direction.northwest)) {
        res += "\\     "
      }
      else {
        res += "      "
      }
      if (i.parent.connections.contains(direction.north)) {
        res += "|"
      }
      else {
        res += " "
      }
      if (i.parent.connections.contains(direction.northeast)) {
        res += "      /"
      }
      else {
        res += "       "
      }
    }
    res + "\n"
  }

  def print_lower_conn(tiles: List[Tile]): String = {
    var res = ""
    for (i <- tiles) {
      if (i.parent.connections.contains(direction.southwest)) {
        res += "/     "
      }
      else {
        res += "      "
      }
      if (i.parent.connections.contains(direction.south)) {
        res += "|"
      }
      else {
        res += " "
      }
      if (i.parent.connections.contains(direction.southeast)) {
        res += "      \\"
      }
      else {
        res += "       "
      }
    }
    res + "\n"
  }

  def print_horizontal(tiles: List[Tile]): String = {
    var res = ""
    for (i <- tiles) {
      res += "  +--------+  "
    }
    res + "\n"
  }

  def print_upper_area(tiles: List[Tile]): String = {
    var res = ""
    for (i <- tiles) {
      if (i.parent.connections.contains(direction.west)) {
        res += "__| "
      }
      else {
        res += "  | "
      }
      res += i.owner + " " + i.soldiers + " " * (6 - i.owner.length - i.soldiers.toString.length)
      if (i.parent.connections.contains(direction.east)) {
        res += "|__"
      }
      else {
        res += "|  "
      }
    }
    res + "\n"
  }

  def print_lower_area(tiles: List[Tile]): String = {
    var res = ""
    for (i <- tiles) {
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
  
  def print_map(data: List[List[Tile]]): String ={
    var output = ""
    for e <- data do {
      output += print_row(e)
    }
    return output
  }
}
