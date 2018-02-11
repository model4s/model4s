package com.github.model4s

import com.github.model4s.Converter.Mappable

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context
import com.twitter.bijection._

/**
  * Bijection: Case Class <-> Map (bijection from com.twitter.bijection-core)
  */
object Converter {
  type Mappable = Map[String, Any]
  def transform[T](implicit m: MapClassPair[T]) = Bijection.build[T, Mappable](m.toMap)(m.fromMap)
}

/**
  * Case Class <-> Map compile time transformer
  * for conversion instance of case class to Map[String, Any]
  * and Map[String, Any] to case class with casting to the proper type
  * @tparam T type of case class
  */
trait MapClassPair[T] {
  def toMap(t: T): Mappable
  def fromMap(map: Mappable): T
}

/**
  * Logic with Scala's macro for Case Class <-> Map conversion
  */
object MapClassPair {
  implicit def materializeMappable[T]: MapClassPair[T] = macro materializeMappableImpl[T]

  def materializeMappableImpl[T: c.WeakTypeTag](c: Context): c.Expr[MapClassPair[T]] = {
    import c.universe._
    val tpe = weakTypeOf[T]
    val companion = tpe.typeSymbol.companion

    val fields = tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor ⇒ m
    }.get.paramLists.head

    /**
      * Names of method's parameters in quasiquotes must match with the same name in the final quasiquote
      */
    val (toMapParams, fromMapParams) = fields.map { field ⇒
      val name = field.name.toTermName
      val decoded = name.decodedName.toString
      val returnType = tpe.decl(name).typeSignature

      (q"$decoded -> t.$name", q"map($decoded).asInstanceOf[$returnType]")
    }.unzip

    c.Expr[MapClassPair[T]] { q"""
      new MapClassPair[$tpe] {
        def toMap(t: $tpe): Mappable = Map(..$toMapParams)
        def fromMap(map: Mappable): $tpe = $companion(..$fromMapParams)
      }
    """ }
  }
}