package com.github.model4s

import com.github.model4s.modelfree.Base._
import com.github.model4s.modelfree.ModelFree
import com.github.model4s.modelfree.Rest.get

object ModelFreeMain extends App {

  @ModelFree case class Person(
                                @dao      @get id: Int,
                                @dao @dto @get name: String,
                                @dao @dto email: String
                           )

  val personDao = Person.Dao(id = 0, name = "Steve", email = "steve@example.com")
  val personDto = Person.Dto(name = "Bill", email = "Bill@example.com")
  val personGet = Person.Get(id = 0, name = "Steve")

  println(personDao)
  println(personDto)
  println(personGet)
}
