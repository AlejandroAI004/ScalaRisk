package model
import controller.*
import view.*

sealed trait GameEvent
case object PlaceInfantryEvent extends GameEvent
case object AttackEvent extends GameEvent

trait GameState {
  def name: String
  def handle(controller: GameController, players: List[Player], event: GameEvent): GameState
}

case object PlacementState extends GameState {
  override val name: String = "Placement"

  override def handle(controller: GameController, players: List[Player], e: GameEvent): GameState = {
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

case object OffenseState extends GameState {
  override val name: String = "Offense"

  override def handle(controller: GameController, players: List[Player], e: GameEvent): GameState = {
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
