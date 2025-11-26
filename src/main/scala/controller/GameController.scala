package controller
import model.*

import scala.util.Random

class GameController(var mapData: List[List[Tile]], var players: List[player]) {
  private var observers: List[Observer] = Nil


  def placeInfantry(
                     player: player, x: Int, y: Int, n: Int
                   ): Either[String, List[List[Tile]]] = {
    if (x < 0 || x >= mapData.head.length || y < 0 || y >= mapData.length)
      Left("Invalid coordinates.")
    else if (n > player.infantry)
      Left("You don't have that many infantry remaining!")
    else if (mapData(y)(x).player != player && mapData(y)(x).player.colorName != "empty") {
      Left("Another Player owns this Tile!")
    } else {
      val updated = updateTile(player, n, mapData(y)(x))
      val newRow = mapData(y).updated(x, updated)
      mapData = mapData.updated(y, newRow)
      player.infantry -= n
      notifyObservers()
      Right(mapData)
    }
  }

  def offense_phase(
                     player: player,
                     fromX: Int, fromY: Int,
                     toX: Int, toY: Int,
                     n: Int
                   ): Either[String, List[List[Tile]]] = {

    if (fromX < 0 || fromX >= mapData.head.length || fromY < 0 || fromY >= mapData.length ||
      toX < 0 || toX >= mapData.head.length || toY < 0 || toY >= mapData.length)
      return Left("Invalid coordinates.")

    val fromTile = mapData(fromY)(fromX)
    val toTile = mapData(toY)(toX)

    if (fromTile.player != player)
      return Left("You can only attack from your own tiles!")

    if (fromTile.soldiers <= 1)
      return Left("You need more than 1 infantry on the attacking tile!")

    if (n <= 0)
      return Left("You must attack with at least 1 infantry!")

    if (n >= fromTile.soldiers) {
      return Left("You must leave at least one infantry on the attacking tile!")
    }

    if (toTile.player == player || toTile.player.colorName == "empty")
      return Left("You can only attack enemy tiles!")

    if(n <= toTile.soldiers) {
      return Left("You dont have more infantry than your opponent!")
    }
    
    // Nachbarschaft über Parent_Tile/ connections prüfen

    val (newFromTile,newToTile) = DiceCombatStrategy.resolveAttack(fromTile,toTile,n)

//    val newFromTile = fromTile.copy(soldiers = fromTile.soldiers - n)
//    val newToTile = Tile(toTile.parent, player, n)

    val rowFromUpdated = mapData(fromY).updated(fromX, newFromTile)
    val tmpMap = mapData.updated(fromY, rowFromUpdated)
    val rowToUpdated = tmpMap(toY).updated(toX, newToTile)
    val newMap = tmpMap.updated(toY, rowToUpdated)
    mapData = newMap
    notifyObservers()
    Right(mapData)
  }

  object DiceCombatStrategy extends CombatStrategy {

    private val rnd = new Random()

    override def resolveAttack(
                                attacker: Tile,
                                defender: Tile,
                                soldiers: Int
                              ): (Tile, Tile) = {
      val attSoldiers = soldiers
      val defSoldiers = defender.soldiers

      val total = attSoldiers + defSoldiers
      val winProb: Double =
        if (total == 0) 0.5
        else attSoldiers.toDouble / total.toDouble

      val attackerWins = rnd.nextDouble() < winProb

      if (attackerWins) {
        // Angreifer gewinnt: verteidiger verliert alles,
        // Angreifer verliert anteilig, aber weniger, wenn er deutlich stärker war
        val lossFactor = 1.0 - winProb // je stärker, desto kleiner
        val attLoss = math.max(1, (attSoldiers * lossFactor).round.toInt)
        val survivors = attSoldiers - attLoss

        val newFrom = attacker.copy(soldiers = attacker.soldiers - attSoldiers)
        val newTo = Tile(defender.parent, attacker.player, survivors.max(1))

        (newFrom, newTo)
      } else {
        // Verteidiger hält: Angreifer verliert hauptsächlich seine Angriffstruppen,
        // Verteidiger verliert auch etwas, abhängig von Verhältnis
        val defLossFactor = winProb // je stärker Angreifer, desto mehr verliert Verteidiger auch
        val defLoss = math.max(0, (defSoldiers * defLossFactor).round.toInt)
        val newDefTroops = (defSoldiers - defLoss).max(1)

        val newFrom = attacker.copy(soldiers = attacker.soldiers - attSoldiers)
        val newTo = defender.copy(soldiers = newDefTroops)

        (newFrom, newTo)
      }
    }
  }

  def addObserver(o: Observer): Unit =
    observers = o :: observers

  def removeObserver(o: Observer): Unit =
    observers = observers.filterNot(_ eq o)

  protected def notifyObservers(): Unit =
    observers.foreach(_.update())


  def allPlayers: List[player] = players
  def tiles: List[List[Tile]] = mapData
}