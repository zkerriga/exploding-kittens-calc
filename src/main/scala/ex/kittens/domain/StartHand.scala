package ex.kittens.domain

import ex.kittens.domain.deck.{Card, DeckNumbers}
import CardOrdering.given

sealed trait StartHand:
  def cards: List[Card.Base]

object StartHand:
  case class Selecting(cards: List[Card.Base]) extends StartHand:
    def add(card: Card.Base)(using rules: DeckNumbers): StartHand =
      val updated = (card :: cards).sorted[Card.Base]
      if updated.size < rules.startCards then Selecting(updated)
      else Complete(updated)

  case class Complete(cards: List[Card.Base]) extends StartHand
