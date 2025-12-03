package model

import controller.GameController

abstract class TurnTemplate {

  final def executeTurn(player: Player, controller: GameController): Unit = {
    preTurn(player, controller)
    doTurn(player, controller)
    postTurn(player, controller)
  }

  protected def preTurn(player: Player, controller: GameController): Unit = {}

  protected def doTurn(player: Player, controller: GameController): Unit

  protected def postTurn(player: Player, controller: GameController): Unit = {}
}
