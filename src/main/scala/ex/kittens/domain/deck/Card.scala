package ex.kittens.domain.deck

import io.circe.syntax.*
import io.circe.{Decoder, Encoder}
import org.latestbit.circe.adt.codec.*

sealed trait Card

object Card:
  enum Active extends Card derives JsonTaggedAdt.PureCodec:
    case Defuse, ExplodingKitten

  enum Base extends Card derives JsonTaggedAdt.PureCodec:
    case Skip
    case Nope
    case TacoCat
    case Shuffle
    case Attack
    case SeeTheFuture
    case Favor
    case BeardCat
    case HairyPotatoCat
    case Cattermelon
    case RainbowRalphingCat

  export Base.*
  export Active.*

  type Defuse = Defuse.type
  type Kitten = ExplodingKitten.type

  given Encoder[Card] = Encoder.instance:
    case base: Base     => base.asJson
    case active: Active => active.asJson

  given Decoder[Card] = Decoder.instance: c =>
    c.as[Base] orElse c.as[Active]
