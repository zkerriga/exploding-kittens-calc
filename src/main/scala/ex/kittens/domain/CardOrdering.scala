package ex.kittens.domain

import ex.kittens.domain.deck.Card

object CardOrdering:
  private val priorityMapping: Map[Card, Int] = Seq(
    Card.ExplodingKitten,
    Card.Defuse,
    Card.Nope,
    Card.Skip,
    Card.Attack,
    Card.SeeTheFuture,
    Card.Shuffle,
    Card.Favor,
    Card.TacoCat,
    Card.BeardCat,
    Card.HairyPotatoCat,
    Card.Cattermelon,
    Card.RainbowRalphingCat,
  ).zipWithIndex.toMap

  given Ordering[Card] = Ordering[Int].on[Card](priorityMapping.getOrElse(_, 0))
  given Ordering[Card.Base] = Ordering[Card].on[Card.Base](identity)
