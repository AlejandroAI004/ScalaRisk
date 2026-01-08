package model.player

import model.colorText.imp1.colorText.colorText

class playerList (list: List[Player] = List()) {
  def addPlayer(player: Player): playerList = {
    new playerList(list :+ player)
  }
  def addPlayer(playerColor: String): playerList = {
    new playerList(list :+ Player(playerColor))
  }
  def usedColors(): List[String] = {
    list.map(_.colorName)
  }
  def toList: List[Player] = list
  override def toString: String = {
    var output = "List of players: \n"
    for i <- list.indices do {
      val p = list(i)
      output += s"${colorText(s"Player ${i + 1}", p.colorName)} -> ${colorText(p.colorName, p.colorName)} " +
        s"| Infantry: ${p.infantry}\n"
    }
    output
  }
}
