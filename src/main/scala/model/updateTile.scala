package model

import model.*

def updateTile(player: player, n: Int, tile: Tile): Tile = {
    Tile(tile.parent, player, tile.soldiers + n)
}