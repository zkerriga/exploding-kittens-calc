package ex.kittens.domain.game

import ex.kittens.domain.deck.{Card, CardInGame}

case class Game2(
    table: Table,
    myHand: AvailableHand,
    playerHand: PlayerHand,
) {
  def playerTookCard: Either[String, Game2] =
    for
      (newTable, card) <- table.cardIsTaken
      cardInHand <- card match
        case CardInGame.Hidden => Right(CardInHand.Hidden)
        case CardInGame.Revealed(card) =>
          card match
            case Card.ExplodingKitten            => Left("player couldn't take kitten card")
            case card: (Card.Base | Card.Defuse) => Right(CardInHand.Revealed(card))
    yield copy(table = newTable, playerHand = playerHand.cardAdded(cardInHand))

  def iTookCard(card: Card.Base | Card.Defuse): Either[String, Game2] =
    for
      (newTable, deckCard) <- table.cardIsTaken
      _ <- deckCard match
        case CardInGame.Hidden             => Right(())
        case CardInGame.Revealed(revealed) => Either.cond(card == revealed, (), "taken card doesn't match")
    yield copy(table = newTable, myHand = myHand.cardAdded(card))
}
