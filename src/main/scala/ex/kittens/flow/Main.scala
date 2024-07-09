package ex.kittens.flow

import cats.effect.{IO, IOApp}
import ex.kittens.domain.deck.DeckNumbers

object Main extends IOApp.Simple:
  val run: IO[Unit] =
    for
      engine <- GameEngine.of[IO](DeckNumbers.twoPlayers)
      server <- GameServer.run[IO](engine)
    yield ()
