package controller

import model.{Tile, colorText, direction}

object Map_Generation {
  def print_upper_conn(tiles: List[Tile]): String = {
    tiles.map { t =>
      (if (t.parent.connections.contains(direction.northwest)) "\\     " else "      ") +
        (if (t.parent.connections.contains(direction.north)) "|" else " ") +
        (if (t.parent.connections.contains(direction.northeast)) "      /" else "       ")
    }.mkString("") + "\n"
  }

  def print_lower_conn(tiles: List[Tile]): String = {
    tiles.map { t =>
      (if (t.parent.connections.contains(direction.southwest)) "/     " else "      ") +
        (if (t.parent.connections.contains(direction.south)) "|" else " ") +
        (if (t.parent.connections.contains(direction.southeast)) "      \\" else "       ")
    }.mkString("") + "\n"
  }

  def print_horizontal(tiles: List[Tile]): String = {
    var res = ""
    for (i <- tiles) {
      res += "  +--------+  "
    }
    res + "\n"
  }

  def print_upper_area(tiles: List[Tile]): String = {
    tiles.map { t =>
      (if (t.parent.connections.contains(direction.west)) "__| " else "  | ") +
        colorText(t.player.colorName, t.player.colorName) + " " + t.soldiers + " " *
        (6 - t.player.colorName.length - t.soldiers.toString.length) +
        (if (t.parent.connections.contains(direction.east)) "|__" else "|  ")
    }.mkString("") + "\n"
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
    output
  }
}
