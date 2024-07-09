package ex.kittens.domain.deck

sealed trait Card

object Card:
  case object ExplodingKitten extends Card

  case object Defuse extends Card
  type Defuse = Defuse.type

  sealed trait Base extends Card

  case object Skip extends Card with Base
  case object Nope extends Card with Base
  case object TacoCat extends Card with Base
  case object Shuffle extends Card with Base
  case object Attack extends Card with Base
  case object SeeTheFuture extends Card with Base
  case object Favor extends Card with Base
  case object BeardCat extends Card with Base
  case object HairyPotatoCat extends Card with Base
  case object Cattermelon extends Card with Base
  case object RainbowRalphingCat extends Card with Base
