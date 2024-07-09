package ex.kittens.flow

import cats.syntax.all.*

import scala.reflect.ClassTag

object StateValidations:
  def phaseV[Phase <: GameState](state: GameState)(using tag: ClassTag[Phase]): Either[ErrorMsg, Phase] =
    tag.unapply(state).toRight {
      ErrorMsg(s"current phase is ${state.getClass.getSimpleName} but ${tag.runtimeClass.getSimpleName} was expected")
    }
