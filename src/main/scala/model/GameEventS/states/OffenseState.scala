package model.GameEventS.states

import controller.GameController.GameControllerPort
import model.*
import model.GameEventS.*
import model.player.Player
import view.*

case object OffenseState extends GameStatePort {
  override val name: String = "Offense"

  override def handle(controller: GameControllerPort, players: List[Player], e: GameEvent): GameStatePort = {
    e match {
      case PlaceInfantryEvent =>
        ConsoleView.showStatus("You cannot place infantry in offense phase.")
        this

      case AttackEvent =>
        ConsoleView.offense_phaseFunctional(players, controller)
        this
    }
  }
}
