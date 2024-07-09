package ex.kittens.domain.game

import ex.kittens.domain.deck.Card

case class AvailableHand(
    cards: Seq[Card.Base | Card.Defuse],
) {
  def cardAdded(card: Card.Base | Card.Defuse): AvailableHand = copy(cards = cards :+ card)

  override def toString: String = cards.map(_.toString).mkString("[", ", ", "]")
}
