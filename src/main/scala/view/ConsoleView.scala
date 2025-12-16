package view

import scala.util.{Failure, Success, Try}
import model.*
import controller.*
import controller.GameController
import view.GUIView.createBoardScene

import scala.annotation.tailrec

object ConsoleView extends Observer{
  private var controller: GameControllerPort = _

  def init(ctrl: GameControllerPort): Unit = {
    controller = ctrl
    controller.add(this)
  }

  override def update(): Unit = {
    val map = controller.tiles
    println(showTileMap(map))
  }
  def welcome(): String = {
    "*** Welcome to Risk! ***\n" +
      "Bei Risk kämpfst du um die Weltherrschaft! \n" +
      "Du platzierst Armeen, planst Angriffe und würfelst um den Sieg. \n" +
      "Mit geschickter Strategie und etwas Glück eroberst du nach und \n" +
      "nach neue Länder und Kontinente. Jede Runde bringt neue Truppen, \n" +
      "spannende Kämpfe und riskante Entscheidungen. Wer am Ende die \n" +
      "meisten Gebiete kontrolliert – oder seine geheime Mission erfüllt \n" +
      "–, gewinnt das Spiel und herrscht über die Welt!\n"
  }

  def start(controller: GameControllerPort): playerList = {
    val numPlayers = askPlayerCount()
    val manager    = new PlayerConfigManager

    while (manager.size < numPlayers) {
      val idx   = manager.size + 1
      val used  = manager.list.usedColors()
      println(s"Spieler $idx von $numPlayers")
      println(s"Aktuelle Spieler:\n${manager.list}")
      val input = scala.io.StdIn.readLine("Farbe eingeben (red, blue, pink, green) oder u=undo, r=redo: \n")
      input match {
        case "u" => manager.undo()
        case "r" => manager.redo()
        case color if used.contains(color) =>
          println("Diese Farbe ist schon vergeben")
        case color =>
          manager.addPlayer(color)
      }
    }

    println("Endgültige Spieler:")
    println(manager.list.toString)


    val colors: List[String] = manager.list.usedColors()
    controller.startGame(numPlayers, colors) match {
      case Success(plist) =>
        gamePhaseLoop()
        plist
      case Failure(ex) =>
        println(s"Fehler: ${ex.getMessage}")
        manager.list
    }
  }

  object ConsoleOffenseTurn extends TurnTemplate {

    override def preTurn(player: Player, controller: GameControllerPort): Unit = {
      showStatus(s"It's ${player.colorName}'s attack phase")
      showTileMap(controller.tiles)
    }

    override def doTurn(player: Player, controller: GameControllerPort): Unit = {
      controller.handleEvent(AttackEvent)
    }

    override def postTurn(player: Player, controller: GameControllerPort): Unit = {
      showStatus(s"Turn finished")
    }
  }

  def askPlayerCount(): Int = {
    println("How many players are gonna play? (min 2,limit 4)")
    scala.io.StdIn.readInt()
  }

  def askForInfantryPlacement(player: Player): (Int, Int, Int) = {
    println(s"\n${colorText(player.colorName, player.colorName)}, you have ${player.infantry} infantry to place.")
    println(s"Remaining infantry: ${player.infantry}")
    var x = readIntSafe("Enter X coordinate (0 to 1):")
    var y = readIntSafe("Enter Y coordinate (0 to 1):")
    var n = readIntSafe("How many infantry to place here?")
    (x, y, n)
  }

  def askForOffenseMove(player: Player): (Int, Int, Int, Int, Int) = {
    println(s"\n${colorText(player.colorName, player.colorName)}, choose your attack:")
    val fromX = readIntSafe("Enter FROM X coordinate:")
    val fromY = readIntSafe("Enter FROM Y coordinate:")
    val toX = readIntSafe("Enter TO   X coordinate:")
    val toY = readIntSafe("Enter TO   Y coordinate:")
    val n = readIntSafe("How many infantry to attack with?")
    (fromX, fromY, toX, toY, n)
  }

  @tailrec
  def placeInfantryFunctional(
                               players: List[Player],
                               controller: GameControllerPort
                             ): List[List[Tile]] = {
    val mapData = controller.tiles
    if (players.forall(_.infantry <= 0))
      mapData
    else {
      val player = players.head
      if (player.infantry > 0) {
        val (x, y, n) = askForInfantryPlacement(player)
        controller.placeInfantry(player, x, y, n) match {
          case Success(newMap) =>
            placeInfantryFunctional(players.tail :+ player, controller)
          case Failure(ex) =>
            showStatus(ex.getMessage)
            print(showTileMap(mapData))
            placeInfantryFunctional(players, controller)
        }
      } else {
        placeInfantryFunctional(players.tail :+ player, controller)
      }
    }
  }

  @tailrec
  def offense_phaseFunctional(players: List[Player],
                              controller: GameControllerPort
                             ): List[List[Tile]] = {
    val mapData = controller.tiles
    val anyCanAttack =
      mapData.exists(row => row.exists(t => t.player.colorName != "empty" && t.soldiers > 1))

    if (!anyCanAttack) {
      mapData
    } else {
      val player = players.head

      // prüfen, ob dieser Spieler überhaupt irgendwo ein angreifbares Feld hat
      val playerCanAttack =
        mapData.exists(row => row.exists(t => t.player == player && t.soldiers > 1))

      if (playerCanAttack) {
        val (fromX, fromY, toX, toY, n) = askForOffenseMove(player)

        controller.offense_phase(player, fromX, fromY, toX, toY, n) match {
          case Success(newMap) =>
            offense_phaseFunctional(players.tail :+ player, controller)

          case Failure(ex) =>
            showStatus(ex.getMessage)
            print(showTileMap(mapData))
            offense_phaseFunctional(players, controller)
        }
      } else {
        offense_phaseFunctional(players.tail :+ player, controller)
      }
    }
  }
  
  def showPlayers(playerList: playerList): String = {
    playerList.toString
  }

  def showTileMap(mapData: List[List[Tile]]): String = {
    Map_Generation.print_map(mapData)
  }

  def showStatus(msg: String): String =  {
    println(msg)
    msg
  }
  
  @tailrec
  def readIntSafe(prompt: String): Int = {
    println(prompt)
    Try(scala.io.StdIn.readInt()) match {
      case Success(value) =>
        value
      case Failure(_) =>
        println("Bitte gib eine gültige Zahl ein!")
        readIntSafe(prompt)
    }
  }

  @tailrec
  private def placeInfantryLoop(controller: GameControllerPort): Unit = {
    if (controller.allInfantryPlaced) {
      println("All infantry placed! Offense phase starts.")
      return
    }

    val player = controller.currentPlayer
    val (x, y, n) = askForInfantryPlacement(player)
    controller.placeInfantry(player, x, y, n) match {
      case Success(_) =>
        controller.nextPlayerTurn()
        placeInfantryLoop(controller)
      case Failure(ex) =>
        println(ex.getMessage)
        placeInfantryLoop(controller)
    }
  }

  @tailrec
  private def offenseTurnLoop(controller: GameControllerPort): Unit = {
    val player = controller.currentPlayer
    println(s"${player.colorName}'s Offense Turn:")
    println("1 = Attack, 0 = End Turn")

    val choice = scala.io.StdIn.readLine("Choice: ").toIntOption.getOrElse(-1)

    choice match {
      case 1 =>
        val (fromX, fromY, toX, toY, n) = askForOffenseMove(player)
        controller.offense_phase(player, fromX, fromY, toX, toY, n) match {
          case Success(_) =>
            println("Attack successful!")
            offenseTurnLoop(controller) 
          case Failure(ex) =>
            println(ex.getMessage)
            println(showTileMap(controller.tiles))
            offenseTurnLoop(controller)
        }
      case 0 =>
        println("Ending offense turn...")
        controller.endOffenseTurn() // nextPlayer + ggf. Reinforcement
      case _ =>
        println("Invalid choice")
        offenseTurnLoop(controller)
    }
  }
  
  @tailrec
  private def gamePhaseLoop(): Unit = {
    controller.currentPhase match {
      case GamePhase.Placement =>
        placeInfantryLoop(controller)
        gamePhaseLoop()
      case GamePhase.Offense =>
        offenseTurnLoop(controller)
        gamePhaseLoop()
      case GamePhase.GameOver =>
        println(s"Game Over! Winner: ${controller.checkWinner()}")
    }
  }

}