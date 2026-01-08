package model.GameEventS

sealed trait GameEvent
case object PlaceInfantryEvent extends GameEvent
case object AttackEvent extends GameEvent
