package ex.kittens.domain.game

case class PlayerHand(
    cards: Seq[CardInHand],
) {
  def cardAdded(card: CardInHand): PlayerHand = copy(cards = card +: cards)

  override def toString: String = cards.map(_.show).mkString("[", ", ", "]")
}
