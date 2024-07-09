package ex.kittens.domain.deck

enum CardInGame:
  case Hidden
  case Revealed(card: Card)

object CardInGame:
  extension (cards: List[CardInGame])
    def countRevealed(p: Card => Boolean): Int = cards.count {
      case CardInGame.Revealed(card) => p(card)
      case CardInGame.Hidden         => false
    }
    def countRevealedIs(card: Card): Int = countRevealed(_ == card)
    def countRevealedIsNot(card: Card): Int = countRevealed(_ != card)

  extension (card: CardInGame)
    def show: String = card match
      case CardInGame.Hidden         => "X"
      case CardInGame.Revealed(card) => card.toString
