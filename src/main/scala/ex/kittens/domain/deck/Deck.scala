package ex.kittens.domain.deck

import ex.kittens.domain.Chance

case class Deck(cards: List[CardInGame]) {
  def taken1: Either[String, (Deck, CardInGame)] = cards match
    case topCard :: next => Right(Deck(next) -> topCard)
    case Nil             => Left("deck is empty")

  def revealed3(top: Card, second: Card, third: Card): Deck =
    Deck(CardInGame.Revealed(top) :: CardInGame.Revealed(second) :: CardInGame.Revealed(third) :: cards.drop(3))

  def shuffled: Deck = Deck(List.fill(cards.size)(CardInGame.Hidden))

  def chanceToExplode(rules: DeckNumbers): Chance = cards match
    case CardInGame.Hidden :: downCards =>
      Chance.ofFraction(
        rules.kittens - downCards.countRevealedIs(Card.ExplodingKitten),
        cards.size - downCards.countRevealedIsNot(Card.ExplodingKitten),
      )
    case CardInGame.Revealed(Card.ExplodingKitten) :: _ => Chance.Sure
    case _                                              => Chance.Zero

  override def toString: String = cards.map(_.show).mkString("[", ", ", "]")
}
