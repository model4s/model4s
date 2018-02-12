package com.github.model4s.modelfree

import org.scalatest._
import Matchers._

class ModelFreeSuite extends FunSuite {

  test("DAO and DTO models") {
    @ModelFree case class Person(
                                  @DAO @DTO id: String,
                                  @DTO name: String
                                )

    """Person.DAO("1ee-44-4")""" should compile
    """Person.DTO("1ee-44-4", "Bill")""" should compile

    """Person.DTO(12, "Bill")""" shouldNot compile
  }

  test("DAO and DTO models with named parameters") {
    @ModelFree case class Person(
                                  @DAO id: Int,
                                  @DAO @DTO name: String,
                                  @DAO @DTO email: String
                                )
    """Person.DAO(id = 0, name = "Steve", email = "steve@example.com")""" should compile
    """Person.DTO(name = "Bill", email = "Bill@example.com")""" should compile

    """Person.DTO(nameS = "Bill", email = "Bill@example.com")""" shouldNot compile
    """Person.DTO(name = "Bill", emailS = "Bill@example.com")""" shouldNot compile
  }

  test ("Instance of generated class") {

    @ModelFree case class Person(
                                  @DAO id: Int,
                                  @DAO @DTO name: String,
                                  @DAO @DTO email: String
                                )

    Person.DAO(id = 0, name = "Steve", email = "steve@example.com")
    Person.DTO(name = "Bill", email = "Bill@example.com")

    """@ModelFree def sqrt(x:Double) = math.sqrt(x)""" shouldNot compile
    """@ModelFree val age = 30 """" shouldNot compile

  }
}
