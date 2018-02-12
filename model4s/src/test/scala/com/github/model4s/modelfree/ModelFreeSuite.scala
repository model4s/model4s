package com.github.model4s.modelfree

import org.scalatest._
import Matchers._

class ModelFreeSuite extends FunSuite {

  test("DAO and DTO models") {
    @ModelFree case class Person(
                                  @dao @dto id: String,
                                  @dto name: String
                                )

    """Person.Dao("1ee-44-4")""" should compile
    """Person.Dto("1ee-44-4", "Bill")""" should compile

    """Person.Dto(12, "Bill")""" shouldNot compile
  }

  test("DAO and DTO models with named parameters") {
    @ModelFree case class Person(
                                  @dao id: Int,
                                  @dao @dto name: String,
                                  @dao @dto email: String
                                )
    """Person.Dao(id = 0, name = "Steve", email = "steve@example.com")""" should compile
    """Person.Dto(name = "Bill", email = "Bill@example.com")""" should compile

    """Person.Dto(nameS = "Bill", email = "Bill@example.com")""" shouldNot compile
    """Person.Dto(name = "Bill", emailS = "Bill@example.com")""" shouldNot compile
  }

  test ("Instance of generated class") {

    @ModelFree case class Person(
                                  @dao id: Int,
                                  @dao @dto name: String,
                                  @dao @dto email: String
                                )

    Person.Dao(id = 0, name = "Steve", email = "steve@example.com")
    Person.Dto(name = "Bill", email = "Bill@example.com")

    """@ModelFree def sqrt(x:Double) = math.sqrt(x)""" shouldNot compile
    """@ModelFree val age = 30 """" shouldNot compile

  }
}
