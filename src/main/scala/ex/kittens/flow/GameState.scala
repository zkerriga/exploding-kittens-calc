package ex.kittens.flow

import ex.kittens.domain.StartHand

sealed trait GameState

object GameState {
  case object NoGame extends GameState
  type NoGame = NoGame.type

  case class SelectingCards(hand: StartHand) extends GameState

  case class Game() extends GameState
}
