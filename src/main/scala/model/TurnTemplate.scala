package model

import controller.{GameController, GameControllerPort}

abstract class TurnTemplate {

  final def executeTurn(player: Player, controller: GameControllerPort): Unit = {
    preTurn(player, controller)
    doTurn(player, controller)
    postTurn(player, controller)
  }

  def preTurn(player: Player, controller: GameControllerPort): Unit

  def doTurn(player: Player, controller: GameControllerPort): Unit

  def postTurn(player: Player, controller: GameControllerPort): Unit
}
