package ex.kittens.domain

import ex.kittens.domain.CardOrdering.given
import ex.kittens.domain.deck.{Card, DeckNumbers}
import ex.kittens.utils.circe.typedEncoder
import io.circe.Encoder
import io.circe.syntax.*

sealed trait StartHand:
  def cards: List[Card.Base]

object StartHand:
  case class Selecting(cards: List[Card.Base]) extends StartHand derives Encoder:
    def add(card: Card.Base)(using rules: DeckNumbers): StartHand =
      val updated = (card :: cards).sorted[Card.Base]
      if updated.size < rules.startCards then Selecting(updated)
      else Complete(updated)

  case class Complete(cards: List[Card.Base]) extends StartHand derives Encoder

  given Encoder[StartHand] = typedEncoder {
    case a: StartHand.Selecting => ("Selecting", a.asJson)
    case a: StartHand.Complete  => ("Complete", a.asJson)
  }
