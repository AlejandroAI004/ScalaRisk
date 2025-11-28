package model

import model.Player

case class Tile(parent: Parent_Tile, player: Player = new Player("empty"), soldiers: Int = 0) {}
