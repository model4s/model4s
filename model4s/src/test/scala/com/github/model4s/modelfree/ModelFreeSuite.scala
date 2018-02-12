package com.github.model4s.modelfree

import org.scalatest._
import Matchers._

class ModelFreeSuite extends FunSuite {

  test("Valid DAO and DTO models") {
    @ModelFree case class Person(
                                  @DAO id: Int,
                                  @DAO @DTO name: String,
                                  @DAO @DTO email: String
                                )

    """Person.DAO(0, "Steve", "steve@example.com")""" should compile
    """Person.DTO("Bill", "Bill@example.com")""" should compile

    """Person.DAO(id = 0, name = "Steve", email = "steve@example.com")""" should compile
    """Person.DTO(name = "Bill", email = "Bill@example.com")""" should compile

    """Person.DTO(nameS = "Bill", email = "Bill@example.com")""" shouldNot compile
    """Person.DTO(name = "Bill", emailS = "Bill@example.com")""" shouldNot compile
  }

}
