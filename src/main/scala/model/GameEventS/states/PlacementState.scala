package model.GameEventS.states

import controller.GameController.GameControllerPort
import controller.GameController.impl1.GameController
import model.*
import model.GameEventS.*
import model.player.Player
import view.*



case object PlacementState extends GameStatePort {
  override val name: String = "Placement"

  override def handle(controller: GameControllerPort, players: List[Player], e: GameEvent): GameStatePort = {
    e match {
      case PlaceInfantryEvent =>
        ConsoleView.placeInfantryFunctional(players, controller)
        if (players.forall(_.infantry <= 0)) OffenseState else this

      case AttackEvent =>
        ConsoleView.showStatus("You cannot attack in placement phase.")
        this
    }
  }
}