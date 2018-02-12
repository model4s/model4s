package com.github.model4s

import scala.annotation.StaticAnnotation
import scala.collection.mutable
import scala.meta._

class DTO extends StaticAnnotation

class DAO extends StaticAnnotation

class ModelFree extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    val (cls, companion) = defn match {
      case q"${cls: Defn.Class}; ${companion: Defn.Object}" => (cls, companion)
      case cls: Defn.Class => (cls, q"object ${Term.Name(cls.name.value)}")
      case _ => abort("@ModelFree must annotate a class")
    }

    val paramsWithAnnotation = for {
      param <- cls.ctor.paramss.flatten
      seenMods = mutable.Set.empty[String]
      modifier <- param.mods if seenMods.add(modifier.toString)
      newParam <- modifier match {
        case mod"@DTO" | mod"@DAO" => Some(param.copy(mods = Nil))
        case _ => None
      }
    } yield modifier -> newParam

    val grouped = paramsWithAnnotation
      .groupBy { case (modifier, _) => modifier.toString() }
      .mapValues(_.map { case (_, param) => param })

    /**
      * Generate necessary case class
      * scala macro api - https://goo.gl/2WGipD
      */
    val models = grouped.map({case (annotation, classParams) =>
      val className = Type.Name(annotation.stripPrefix("@").capitalize)
      q"case class $className[..${cls.tparams}](..$classParams)"
    })

    /**
      * Push case classes (marked by annotation) into companion object of the target class
      */
    val newCompanion = companion.copy(
      templ = companion.templ.copy(
        stats = Some(companion.templ.stats.getOrElse(Nil) ++ models)
      )
    )

    q"$newCompanion"
  }
}
