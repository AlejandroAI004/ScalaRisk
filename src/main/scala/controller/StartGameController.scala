package controller
import model.*
import TUI.*

object StartGameController {
  def start(view: ConsoleView.type): List[player] = {
    val numPlayers = view.askPlayerCount()
    var playersList = List[player]()
    for (i <- 1 to numPlayers) {
      val color = view.askPlayerColor(i, playersList.map(_.colorName))
      val newPlayer = new player(color)
      playersList = playersList :+ newPlayer
    }
    view.printPlayersList(playersList)
    playersList
  }
}
