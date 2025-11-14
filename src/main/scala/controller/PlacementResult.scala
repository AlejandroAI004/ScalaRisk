package controller

import model.Tile

sealed trait InfantryPlacementResult
case object Success extends InfantryPlacementResult
case class allValid(message: List[List[Tile]]) extends InfantryPlacementResult
case class InvalidInput(message: String) extends InfantryPlacementResult
case class TileOccupied(message: String) extends InfantryPlacementResult
