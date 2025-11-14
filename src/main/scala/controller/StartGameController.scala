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

  def print_playersList(playersList: List[player]): String = {
    var output = "List of players: \n"
    for i <- playersList.indices do {
      var p = playersList(i)
      output += s"${colorText(s"Player ${i + 1}", p.colorName)} -> ${colorText(p.colorName, p.colorName)} " +
        s"| Infantry: ${p.infantry}\n"
    }
    output
  }
}
