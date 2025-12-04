package model

import controller.GameController

abstract class TurnTemplate {

  final def executeTurn(player: Player, controller: GameController): Unit = {
    preTurn(player, controller)
    doTurn(player, controller)
    postTurn(player, controller)
  }

  def preTurn(player: Player, controller: GameController): Unit

  def doTurn(player: Player, controller: GameController): Unit

  def postTurn(player: Player, controller: GameController): Unit
}
