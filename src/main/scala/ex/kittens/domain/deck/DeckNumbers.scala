package ex.kittens.domain.deck

trait BaseDeckNumbers:
  def skips: Int
  def nopes: Int
  def tacoCats: Int
  def shuffles: Int
  def attacks: Int
  def seeTheFutures: Int
  def favors: Int
  def beardCats: Int
  def hairyPotatoCats: Int
  def cattermelons: Int
  def rainbowRalphingCats: Int

  final def sum: Int =
    skips + nopes + tacoCats + shuffles + attacks + seeTheFutures + favors + beardCats + hairyPotatoCats + cattermelons + rainbowRalphingCats

trait DeckNumbers:
  def base: BaseDeckNumbers
  def kittens: Int
  def defuses: Int
  final def sum: Int = base.sum + kittens + defuses

  def startCards: Int

object DeckNumbers:
  val DefaultBaseDeck: BaseDeckNumbers = new:
    val tacoCats = 4
    val beardCats = 4
    val hairyPotatoCats = 4
    val cattermelons = 4
    val rainbowRalphingCats = 4
    val favors = 4
    val skips = 4
    val shuffles = 4
    val attacks = 4
    val nopes = 5
    val seeTheFutures = 5

  def twoPlayers: DeckNumbers = new:
    val base = DefaultBaseDeck
    val kittens = 1
    val defuses = 4
    val startCards = 7
