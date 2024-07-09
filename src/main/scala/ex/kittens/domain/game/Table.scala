package ex.kittens.domain.game

import ex.kittens.domain.Chance
import ex.kittens.domain.deck.{Card, CardInGame, Deck, DeckNumbers}

case class Table(
    deck: Deck,
    played: List[Card.Base | Card.Defuse],
    rules: DeckNumbers,
) {
  def cardPlayed(card: Card.Base | Card.Defuse): Table =
    copy(played = card :: played)

  def cardIsTaken: Either[String, (Table, CardInGame)] =
    for (newDeck, card) <- deck.taken1
    yield (copy(deck = newDeck), card)

  def chanceToExplode: Chance = deck.chanceToExplode(rules)
}
