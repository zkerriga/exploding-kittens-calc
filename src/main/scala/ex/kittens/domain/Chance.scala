package ex.kittens.domain

import scala.math.BigDecimal.RoundingMode

trait Chance:
  def probability: BigDecimal
  def percent: BigDecimal = probability * 100

  override def toString: String = s"$percent%"

object Chance:
  def of(value: BigDecimal): Chance = new:
    val probability: BigDecimal = value.setScale(2, RoundingMode.HALF_UP)

  def ofFraction(numerator: Int, denominator: Int): Chance =
    of(BigDecimal(numerator) / denominator)

  val Zero: Chance = of(0)
  val Sure: Chance = of(1)
