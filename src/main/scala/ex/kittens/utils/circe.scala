package ex.kittens.utils

import io.circe.{Encoder, Json}

object circe:
  def typedEncoder[A](to: A => (String, Json)): Encoder[A] = (a: A) =>
    val (typeStr, json) = to(a)
    Json.obj(typeStr -> json)
