package model.mapInit

import model.tile.Tile

trait MapInitPort {
  def createInitialMap(): List[List[Tile]]
}
