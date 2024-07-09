package ex.kittens.flow

import cats.Monad
import cats.data.NonEmptyList
import cats.effect.Ref
import cats.effect.kernel.Sync
import cats.syntax.all.*
import ex.kittens.domain.StartHand
import ex.kittens.domain.deck.{Card, DeckNumbers}
import ex.kittens.flow.GameState.SelectingCards
import ex.kittens.flow.StateValidations.*

import scala.reflect.ClassTag

class GameEngine[F[_]: Monad](states: Ref[F, NonEmptyList[GameState]], rules: DeckNumbers):
  given DeckNumbers = rules

  def getState: F[GameState] = states.get.map(_.head)

  def stopGame: F[GameState] =
    val noGame = GameState.NoGame
    states.set(NonEmptyList.one(noGame)).as(noGame)

  def undo: F[GameState] =
    states
      .updateAndGet { stateHistory =>
        NonEmptyList.fromList(stateHistory.tail).getOrElse(NonEmptyList.one(GameState.NoGame))
      }
      .map(_.head)

  private def update(change: GameState => Either[ErrorMsg, GameState]): F[Either[ErrorMsg, GameState]] =
    states.modify: stateHistory =>
      val current = stateHistory.head
      change(current) match
        case Left(error)    => stateHistory -> error.asLeft
        case Right(changed) => (changed :: stateHistory) -> changed.asRight

  private def updatePhase[Phase <: GameState: ClassTag](
      change: Phase => Either[ErrorMsg, GameState],
  ): F[Either[ErrorMsg, GameState]] = update: state =>
    for
      phase <- phaseV[Phase](state)
      reply <- change(phase)
    yield reply

  def startGame: F[Either[ErrorMsg, GameState]] =
    updatePhase[GameState.NoGame]: _ =>
      GameState.SelectingCards(StartHand.Selecting(List.empty)).asRight

  def selectCard(card: Card.Base): F[Either[ErrorMsg, GameState]] =
    updatePhase[SelectingCards]: selecting =>
      for hand <- selecting.hand match
          case selecting: StartHand.Selecting => selecting.add(card).asRight
          case complete: StartHand.Complete   => ErrorMsg("hand is already complete").asLeft
      yield GameState.SelectingCards(hand)

  def confirmSelectedCards: F[Either[ErrorMsg, GameState]] =
    updatePhase[SelectingCards]: selecting =>
      for hand <- selecting.hand match
          case selecting: StartHand.Selecting => ErrorMsg("hand is not complete").asLeft
          case complete: StartHand.Complete   => complete.asRight
      yield GameState.Game() // todo: set game data

object GameEngine:
  def of[F[_]: Sync](rules: DeckNumbers): F[GameEngine[F]] =
    for state <- Ref.of[F, NonEmptyList[GameState]](NonEmptyList.one(GameState.NoGame))
    yield GameEngine(state, rules)
