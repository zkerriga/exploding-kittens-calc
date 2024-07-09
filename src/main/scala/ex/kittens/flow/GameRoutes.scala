package ex.kittens.flow

import cats.effect.kernel.Sync
import cats.syntax.all.*
import ex.kittens.domain.StartHand
import ex.kittens.domain.deck.Card
import io.circe.syntax.*
import io.circe.{Encoder, Json}
import org.http4s.circe.jsonEncoderOf
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityEncoder, HttpRoutes, Response}

class GameRoutes[F[_]: Sync]:
  private val dsl = new Http4sDsl[F] {}
  import dsl.*

  extension [A](fa: F[Either[ErrorMsg, A]])(using EntityEncoder[F, A])
    def convertErrors: F[Response[F]] = fa.attempt.flatMap:
      case Left(_) => InternalServerError()
      case Right(result) =>
        result match
          case Left(error)  => BadRequest(error.toString)
          case Right(value) => Ok(value)

  extension [A](fa: F[A])(using EntityEncoder[F, A])
    def convertError: F[Response[F]] = fa.map(_.asRight[ErrorMsg]).convertErrors

  given cardEncoder: Encoder[Card] = Encoder.encodeString.contramap(_.toString)
  given Encoder[Card.Base] = cardEncoder.contramap(identity)

  def typedEncoder[A](to: A => (String, Json)): Encoder[A] = (a: A) =>
    val (typeStr, json) = to(a)
    Json.obj(typeStr -> json)

  given Encoder[StartHand.Selecting] = Encoder.derived
  given Encoder[StartHand.Complete] = Encoder.derived
  given Encoder[StartHand] = typedEncoder {
    case a: StartHand.Selecting => ("Selecting", a.asJson)
    case a: StartHand.Complete  => ("Complete", a.asJson)
  }

  given Encoder[GameState.NoGame] = Encoder.derived
  given Encoder[GameState.SelectingCards] = Encoder.derived
  given Encoder[GameState.Game] = Encoder.derived
  given Encoder[GameState] = typedEncoder {
    case a: GameState.NoGame         => ("NoGame", a.asJson)
    case a: GameState.SelectingCards => ("SelectingCards", a.asJson)
    case a: GameState.Game           => ("Game", a.asJson)
  }

  given EntityEncoder[F, GameState] = jsonEncoderOf

  def flowRouts(engine: GameEngine[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "state" => engine.getState.convertError
      case PUT -> Root / "stop"  => engine.stopGame.convertError
      case POST -> Root / "undo" => engine.undo.convertError
    }

  def selectingRoutes(engine: GameEngine[F]): HttpRoutes[F] =
    HttpRoutes.of[F] { case request @ POST -> Root / "selecting" =>
      ???
    }
