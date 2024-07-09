package ex.kittens.flow

import cats.effect.Async
import cats.syntax.all.*
import ex.kittens.domain.deck.Card
import ex.kittens.domain.deck.Card.Base.given
import io.circe.syntax.*
import io.circe.{Decoder, Encoder, Json}
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes, Response}

class GameRoutes[F[_]: Async]:
  private val dsl = new Http4sDsl[F] {}
  import dsl.*

  given Encoder[ErrorMsg] = Encoder.instance(a => Json.obj("error" -> a.error.asJson))
  given EntityEncoder[F, ErrorMsg] = jsonEncoderOf

  extension [A](fa: F[Either[ErrorMsg, A]])(using EntityEncoder[F, A])
    def convertErrors: F[Response[F]] = fa.attempt.flatMap:
      case Left(_) => InternalServerError()
      case Right(result) =>
        result match
          case Left(error)  => BadRequest(error)
          case Right(value) => Ok(value)

  extension [A](fa: F[A])(using EntityEncoder[F, A])
    def convertError: F[Response[F]] = fa.map(_.asRight[ErrorMsg]).convertErrors

  given EntityEncoder[F, GameState] = jsonEncoderOf

  def flowRouts(engine: GameEngine[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "state"  => engine.getState.convertError
      case PUT -> Root / "stop"   => engine.stopGame.convertError
      case POST -> Root / "undo"  => engine.undo.convertError
      case POST -> Root / "start" => engine.startGame.convertErrors
    }

  case class SelectStartHandCard(card: Card.Base) derives Decoder

  given EntityDecoder[F, SelectStartHandCard] = jsonOf

  def selectingRoutes(engine: GameEngine[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case request @ POST -> Root / "select" =>
        for
          selectCard <- request.as[SelectStartHandCard].attempt
          response <- selectCard match
            case Right(selectCard) => engine.selectCard(selectCard.card).convertErrors
            case Left(error)       => BadRequest(ErrorMsg(error.getMessage))
        yield response
      case PUT -> Root / "select" / "confirm" =>
        engine.confirmSelectedCards.convertErrors
    }
