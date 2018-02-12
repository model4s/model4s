package com.github.model4s.modelfree

import scala.annotation.StaticAnnotation
import scala.collection.mutable
import scala.meta._

/**
  * Scala macros to generate oft-repeated models with annotations
  * in similar way to Java's lombok, but in class level
  *  <pre>
  * case class Person(id:Long, name:String, email:String)
  *
  * @ModelFree case class Person( @dao id: Int,
  *                               @dao @dto name: String,
  *                               @dao @dto email: String)
  *
  * //Transforms companion object into this
  * object User {
  *   case class DAO(id: Int, name: String, email: String)
  *   case class DTO(name: String, email: String)
  *   }
  * </pre>
  **/
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
        case mod"@dto" | mod"@dao" |
             mod"@get" | mod"@put" | mod"@post" | mod"@delete" => Some(param.copy(mods = Nil))
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
