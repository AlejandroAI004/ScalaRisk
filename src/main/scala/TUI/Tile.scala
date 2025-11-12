package TUI
import logic.player

case class Tile(parent: Parent_Tile, player: player = new player("empty"), soldiers: Int = 0) {}
