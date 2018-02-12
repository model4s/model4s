package com.github.model4s

import com.github.model4s.modelfree.{dao, dto, ModelFree}

object ModelFreeMain extends App {

  @ModelFree case class Person(
                                @dao id: Int,
                                @dao @dto name: String,
                                @dao @dto email: String
                           )

  val person1 = Person.Dao(id = 0, name = "Steve", email = "steve@example.com")
  val person2 = Person.Dto(name = "Bill", email = "Bill@example.com")
  println(person1)
  println(person2)
}
