package logic

import scala.io.StdIn

object startgame {
  var playersList = Array[player]()
  
  def welcome(): Unit = {
    println("*** Welcome to Risk! ***")
    println("What do you want to do?")
    var valid1 = false
    while !valid1 do {
      println("1.Start")
      println("2.Rules")
      println("3.Leave")
      val inputNumber = StdIn.readInt()
      inputNumber match {
        case 1 => start(); valid1 = true
        case 2 => println("Bei Risk kämpfst du um die Weltherrschaft! \n" +
          "Du platzierst Armeen, planst Angriffe und würfelst um den Sieg. \n" +
          "Mit geschickter Strategie und etwas Glück eroberst du nach und \n" +
          "nach neue Länder und Kontinente. Jede Runde bringt neue Truppen, \n" +
          "spannende Kämpfe und riskante Entscheidungen. Wer am Ende die \n" +
          "meisten Gebiete kontrolliert – oder seine geheime Mission erfüllt \n" +
          "–, gewinnt das Spiel und herrscht über die Welt!\n")
        case 3 => System.exit(0)
        case _ => println("invalid input")
      }
    }
  }

  def start(): Unit = {
    println("How many players are gonna play? (min 2,limit 4)")
    val TotalPlayers = StdIn.readInt()

    for i <- 0 until TotalPlayers do {
      println(s"Enter name for Player ${i + 1}:")
      val name = StdIn.readLine()

      var valid2 = false
      var colorName = "gray"

      while !valid2 do {
        println("Select a color (red, blue, yellow, green):")
        val input = scala.io.StdIn.readLine().toLowerCase()
        input match {
          case "red" | "blue" | "yellow" | "green" =>
            colorName = input
            valid2 = true
          case _ => println("Unknown color, try again!")
        }
        if playersList.exists(p => p.colorName == colorName) then {
          println("that color is taken!")
          valid2 = false
        }
      }

      playersList = playersList :+ new player(name, colorName)
      println(s"${colorText(name, colorName)} has selected ${colorText(colorName, colorName)}" +
        s" and has 20 infantry!")
    }

    println("List of players: ")
    for p <- playersList do {
      println(s"${colorText(p.name, p.colorName)} -> ${colorText(p.colorName, p.colorName)} " +
        s"| Infantry: ${p.infantry}")
    }
    
    TestRisk.risk()
  }

}
