package controller.GameController.impl1

import controller.GameController.GameControllerPort
import model.*
import model.Combat.CombatStrategyPort
import model.Combat.CombatStrategy.DiceCombatStrategy
import model.GameEventS.states.PlacementState
import model.GameEventS.*
import model.mapInit.imp1.MapInit
import model.player.{Player, playerList}
import model.tile.{Tile, updateTile}
import util.command.PlayerConfigManager
import util.gamePhase.GamePhase
import util.observer.Observable

import scala.util.{Failure, Success, Try}

class GameController(var initialMap: List[List[Tile]],
                     var players: List[Player],
                     var combatStrategy: CombatStrategyPort = DiceCombatStrategy) extends Observable with GameControllerPort {

  private var phase: GamePhase = GamePhase.Placement
  def currentPhase: GamePhase = phase
  private var mapData: List[List[Tile]] = initialMap
  private var state: GameStatePort = PlacementState
  var currentPlayerIndex: Int = 0
  def currentPlayer: Player = players(currentPlayerIndex)


  def startGame(numPlayers: Int, colors: List[String]): Try[playerList] = {
    if (numPlayers < 2 || numPlayers > 4)
      Failure(new IllegalArgumentException("Players must be between 2 and 4"))
    else if (colors.distinct.size != colors.size)
      Failure(new IllegalArgumentException("cannot have same colors"))
    else if (colors.size != numPlayers)
      Failure(new IllegalArgumentException("colors size must equal numPlayers"))
    else {
      val manager = new PlayerConfigManager
      colors.foreach(manager.addPlayer)
      val plist: playerList = manager.list

      players = plist.toList
      currentPlayerIndex = 0
      val neutralMap = MapInit.createInitialMap() 
      val flatTiles = neutralMap.flatten 

      val allPlayers = scala.util.Random.shuffle(players) // random Player-Reihenfolge
      val playerIter = Iterator.continually(allPlayers).flatten

      val shuffledTilesWithIndex = scala.util.Random.shuffle(
        flatTiles.zipWithIndex
      )
      
      val filledFlat: Array[Tile] = Array.ofDim[Tile](flatTiles.size)

      for ((tile, idx) <- shuffledTilesWithIndex) {
        val p = playerIter.next()
        filledFlat(idx) = tile.copy(player = p, soldiers = 1)
      }

      
      val width = neutralMap.head.length
      val height = neutralMap.length
      val assignedMap: List[List[Tile]] =
        filledFlat.grouped(width).map(_.toList).toList

      mapData = assignedMap
      phase = GamePhase.Placement
      notifyObservers()
      Success(plist)
    }
  }
  def placeInfantry(
                     player: Player, x: Int, y: Int, n: Int
                   ): Try[List[List[Tile]]] = {

    if (phase != GamePhase.Placement)
      return Failure(new IllegalStateException("Cannot place infantry now"))

    if (x < 0 || x >= mapData.head.length || y < 0 || y >= mapData.length)
      Failure(new IllegalArgumentException("Invalid coordinates."))
    else if (n > player.infantry)
      Failure(new IllegalArgumentException("You don't have that many infantry remaining!"))
    else if (mapData(y)(x).player != player && mapData(y)(x).player.colorName != "empty")
      Failure(new IllegalArgumentException("Another Player owns this Tile!"))
    else {
      val updated = updateTile(player, n, mapData(y)(x))
      val newRow = mapData(y).updated(x, updated)
      mapData = mapData.updated(y, newRow)
      player.infantry -= n
      if (allInfantryPlaced && phase == GamePhase.Placement) {
        phase = GamePhase.Offense
      }
      notifyObservers()
      Success(mapData)
    }
  }

  def offense_phase(
                     player: Player,
                     fromX: Int, fromY: Int,
                     toX: Int, toY: Int,
                     n: Int
                   ): Try[List[List[Tile]]] = {

    if (phase != GamePhase.Offense)
      return Failure(new IllegalStateException("Not in offense phase"))

    if (fromX < 0 || fromX >= mapData.head.length || fromY < 0 || fromY >= mapData.length ||
      toX < 0 || toX >= mapData.head.length || toY < 0 || toY >= mapData.length)
      return Failure(new IllegalArgumentException("Invalid coordinates."))

    val fromTile = mapData(fromY)(fromX)
    val toTile = mapData(toY)(toX)

    if (fromTile.player != player)
      return Failure(new IllegalArgumentException("You can only attack from your own tiles!"))
    if (fromTile.soldiers <= 1)
      return Failure(new IllegalArgumentException("You need more than 1 infantry on the attacking tile!"))

    if (n <= 0)
      return Failure(new IllegalArgumentException("You must attack with at least 1 infantry!"))

    if (n >= fromTile.soldiers) {
      return Failure(new IllegalArgumentException("You must leave at least one infantry on the attacking tile!"))
    }

    if (toTile.player == player || toTile.player.colorName == "empty")
      return Failure(new IllegalArgumentException("You can only attack enemy tiles!"))

    if(n <= toTile.soldiers) {
      return Failure(new IllegalArgumentException("You dont have more infantry than your opponent!"))
    }

    if (!fromTile.parent.neighbours.contains(toTile.parent))
      return Failure(new IllegalArgumentException("You can only attack adjacent tiles!"))

    val (newFromTile,newToTile) = combatStrategy.resolveAttack(fromTile,toTile,n)

    val rowFromUpdated = mapData(fromY).updated(fromX, newFromTile)
    val tmpMap = mapData.updated(fromY, rowFromUpdated)
    val rowToUpdated = tmpMap(toY).updated(toX, newToTile)
    val newMap = tmpMap.updated(toY, rowToUpdated)
    mapData = newMap
    checkWinner() match {
      case Some(_) =>
        phase = GamePhase.GameOver
      case None => ()
    }
    notifyObservers()
    Success(mapData)
  }

  def endOffenseTurn(): Unit = {
    nextPlayerTurn()

    if (currentPlayerIndex == 0) {
      startReinforcementPhase()
    }

    notifyObservers()
  }

  def startReinforcementPhase(): Unit = {
    players.foreach { player =>
      val ownedTiles = mapData.flatten.count(_.player == player)
      val reinforcements = math.max(3, ownedTiles / 3)
      player.infantry += reinforcements
    }

    phase = GamePhase.Placement
    notifyObservers()
  }

  def checkWinner(): Option[Player] = {
    val owners = mapData.flatten.map(_.player).distinct
    if (owners.size == 1 && owners.head.colorName != "empty")
      Some(owners.head)
    else None
  }


  def nextPlayerTurn(): Unit = {
    if (players.nonEmpty) {
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size
      notifyObservers()
    }
  }

  def remainingInfantryPerPlayer: List[(String, Int)] =
    players.map(p => (p.colorName, p.infantry))

  def allInfantryPlaced: Boolean =
    players.forall(_.infantry <= 0)
  def allPlayers: List[Player] = players
  def tiles: List[List[Tile]] = mapData
  def currentStateName: String = state.name
  def handleEvent(e: GameEvent): Unit =
    state = state.handle(this, players, e)

}