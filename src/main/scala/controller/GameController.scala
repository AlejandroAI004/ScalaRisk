package controller
import model.*

import scala.util.{Try, Success, Failure}

class GameController(var initialMap: List[List[Tile]],
                     var players: List[Player],
                     var combatStrategy: CombatStrategy = DiceCombatStrategy) extends Observable {

  private var mapData: List[List[Tile]] = initialMap
  private var state: GameState = PlacementState
  var currentPlayerIndex: Int = 0
  def currentPlayer: Player = players(currentPlayerIndex)


  def startGame(numPlayers: Int, colors: List[String]): playerList = {
    require(numPlayers >= 2 && numPlayers <= 4)
    require(colors.distinct.size == colors.size)
    require(colors.size == numPlayers)

    val manager = new PlayerConfigManager
    colors.foreach(manager.addPlayer)
    val plist: playerList = manager.list

    players = plist.toList // Feld setzen
    currentPlayerIndex = 0 // beim ersten Spieler starten

    notifyObservers()
    plist
  }
  def placeInfantry(
                     player: Player, x: Int, y: Int, n: Int
                   ): Try[List[List[Tile]]] = {

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
    
    // Nachbarschaft über Parent_Tile/ connections prüfen

    val (newFromTile,newToTile) = combatStrategy.resolveAttack(fromTile,toTile,n)

    val rowFromUpdated = mapData(fromY).updated(fromX, newFromTile)
    val tmpMap = mapData.updated(fromY, rowFromUpdated)
    val rowToUpdated = tmpMap(toY).updated(toX, newToTile)
    val newMap = tmpMap.updated(toY, rowToUpdated)
    mapData = newMap
    notifyObservers()
    Success(mapData)
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