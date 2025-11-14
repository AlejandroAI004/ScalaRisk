package model

class playerList (list: List[player] = List()) {
  def addPlayer(player: player): playerList = {
    new playerList(list :+ player)
  }
  def addPlayer(playerColor: String): playerList = {
    new playerList(list :+ new player(playerColor))
  }
  def usedColors(): List[String] = {
    list.map(_.colorName)
  }
  override def toString(): String = {
    var output = "List of players: \n"
    for i <- list.indices do {
      val p = list(i)
      output += s"${colorText(s"Player ${i + 1}", p.colorName)} -> ${colorText(p.colorName, p.colorName)} " +
        s"| Infantry: ${p.infantry}\n"
    }
    output
  }
}
