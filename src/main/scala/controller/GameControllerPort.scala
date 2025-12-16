package controller

import model._
import scala.util.Try

trait GameControllerPort {

  def add(o: Observer): Unit

  // Start / Setup
  def startGame(numPlayers: Int, colors: List[String]): Try[playerList]

  def currentPhase: GamePhase
  def endOffenseTurn(): Unit
  def startReinforcementPhase(): Unit
  

  def tiles: List[List[Tile]]
  def currentPlayer: Player
  def allPlayers: List[Player]
  def remainingInfantryPerPlayer: List[(String, Int)]
  def allInfantryPlaced: Boolean
  def checkWinner(): Option[Player]
  def nextPlayerTurn(): Unit

  def placeInfantry(player: Player, x: Int, y: Int, n: Int): Try[List[List[Tile]]]
  def offense_phase(player: Player,
                    fromX: Int, fromY: Int,
                    toX: Int, toY: Int,
                    n: Int): Try[List[List[Tile]]]

  def handleEvent(e: GameEvent): Unit
  def currentStateName: String
}