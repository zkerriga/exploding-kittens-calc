package ex.kittens.flow

import cats.effect.Async
import cats.syntax.all.*
import com.comcast.ip4s.{ipv4, port}
import fs2.io.net.Network
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger

object GameServer:
  def run[F[_]: Async: Network](engine: GameEngine[F]): F[Nothing] = {
    for
      client <- EmberClientBuilder.default[F].build
      routes = GameRoutes[F]

      httpApp = (
        routes.flowRouts(engine) <+>
          routes.selectingRoutes(engine)
      ).orNotFound
      loggedHttpApp = Logger.httpApp(logHeaders = false, logBody = true)(httpApp)

      _ <- EmberServerBuilder
        .default[F]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(loggedHttpApp)
        .build
    yield ()
  }.useForever
