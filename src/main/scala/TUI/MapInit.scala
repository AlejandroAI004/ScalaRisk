package TUI

import model.{Parent_Tile, Tile, add_neighbour, direction}

object MapInit {
  def testMap_init(): List[List[Tile]] = {
    var konstanz = Parent_Tile(List(), List())
    var meersburg = Parent_Tile(List(konstanz), List(direction.south))
    var friedrichshafen = Parent_Tile(List(meersburg,konstanz), List(direction.west,direction.southwest))
    var kreuzlingen = Parent_Tile(List(konstanz), List(direction.west))
    konstanz = add_neighbour(konstanz, meersburg, direction.north)
    konstanz = add_neighbour(konstanz, friedrichshafen, direction.northeast)
    konstanz = add_neighbour(konstanz, kreuzlingen, direction.east)
    meersburg = add_neighbour(meersburg, friedrichshafen, direction.east)
    val kn = Tile(konstanz)
    val mb = Tile(meersburg)
    val fh = Tile(friedrichshafen)
    val kr = Tile(kreuzlingen)

     List(List(mb, fh), List(kn,kr))
  }
}
