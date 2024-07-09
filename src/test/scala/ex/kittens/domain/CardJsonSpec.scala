package ex.kittens.domain

import ex.kittens.domain.deck.Card
import io.circe.parser.decode
import io.circe.syntax.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class CardJsonSpec extends AnyFlatSpec, Matchers:
  "Card.Base" should "serialize and deserialize from/to strings" in {
    Seq(
      "Skip" -> Card.Skip,
      "Nope" -> Card.Nope,
      "TacoCat" -> Card.TacoCat,
      "Shuffle" -> Card.Shuffle,
      "Attack" -> Card.Attack,
      "SeeTheFuture" -> Card.SeeTheFuture,
      "Favor" -> Card.Favor,
      "BeardCat" -> Card.BeardCat,
      "HairyPotatoCat" -> Card.HairyPotatoCat,
      "Cattermelon" -> Card.Cattermelon,
      "RainbowRalphingCat" -> Card.RainbowRalphingCat,
    ).foreach { case (str, card) =>
      val jsonStr = '"' + str + '"'

      card.asJson.noSpaces mustBe jsonStr
      decode[Card.Base](jsonStr) mustBe Right(card)
    }
  }

  "Card.Active" should "serialize and deserialize from/to strings" in {
    Seq(
      "ExplodingKitten" -> Card.ExplodingKitten,
      "Defuse" -> Card.Defuse,
    ).foreach { case (str, card) =>
      val jsonStr = '"' + str + '"'

      card.asJson.noSpaces mustBe jsonStr
      decode[Card.Active](jsonStr) mustBe Right(card)
    }
  }

  "Card" should "serialize and deserialize from/to strings" in {
    Seq(
      "ExplodingKitten" -> Card.ExplodingKitten,
      "Defuse" -> Card.Defuse,
      "Skip" -> Card.Skip,
      "Nope" -> Card.Nope,
      "TacoCat" -> Card.TacoCat,
      "Shuffle" -> Card.Shuffle,
      "Attack" -> Card.Attack,
      "SeeTheFuture" -> Card.SeeTheFuture,
      "Favor" -> Card.Favor,
      "BeardCat" -> Card.BeardCat,
      "HairyPotatoCat" -> Card.HairyPotatoCat,
      "Cattermelon" -> Card.Cattermelon,
      "RainbowRalphingCat" -> Card.RainbowRalphingCat,
    ).foreach { case (str, card) =>
      val jsonStr = '"' + str + '"'

      card.asJson.noSpaces mustBe jsonStr
      decode[Card](jsonStr) mustBe Right(card)
    }
  }
