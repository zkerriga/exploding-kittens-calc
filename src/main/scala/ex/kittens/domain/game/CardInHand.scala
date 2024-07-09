package ex.kittens.domain.game

import ex.kittens.domain.deck.Card

enum CardInHand:
  case Hidden
  case Revealed(card: Card.Base | Card.Defuse)

object CardInHand:
  extension (card: CardInHand)
    def show: String = card match
      case CardInHand.Hidden         => "X"
      case CardInHand.Revealed(card) => card.toString
