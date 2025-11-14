package controller

import model.*

import scala.io.StdIn.readInt

def updateTile(player: player, n: Int, tile: Tile): Tile = {
    Tile(tile.parent, player, tile.soldiers + n)
}