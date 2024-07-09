package ex.kittens.flow

import ex.kittens.domain.StartHand
import ex.kittens.utils.circe.typedEncoder
import io.circe.Encoder
import io.circe.syntax.*

sealed trait GameState

object GameState {
  case object NoGame extends GameState derives Encoder
  type NoGame = NoGame.type

  case class SelectingCards(hand: StartHand) extends GameState derives Encoder

  case class Game() extends GameState derives Encoder

  // todo: write/find a macros for such codec generation
  given Encoder[GameState] = typedEncoder {
    case a: GameState.NoGame         => ("NoGame", a.asJson)
    case a: GameState.SelectingCards => ("SelectingCards", a.asJson)
    case a: GameState.Game           => ("Game", a.asJson)
  }
}
