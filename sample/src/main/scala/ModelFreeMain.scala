package com.github.model4s

import com.github.model4s.modelfree.{DAO, DTO, ModelFree}

object ModelFreeMain extends App {

  @ModelFree case class Person(
                             @DAO id: Int,
                             @DAO @DTO name: String,
                             @DAO @DTO email: String
                           )

  val person1 = Person.DAO(id = 0, name = "Steve", email = "steve@example.com")
  val person2 = Person.DTO(name = "Bill", email = "Bill@example.com")
  println(person1)
  println(person2)
}
