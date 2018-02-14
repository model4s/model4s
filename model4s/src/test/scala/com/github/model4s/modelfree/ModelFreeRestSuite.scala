package com.github.model4s.modelfree

import com.github.model4s.modelfree.Rest.{delete, get, post, put}
import org.scalatest.Matchers._
import org.scalatest._

class ModelFreeRestSuite extends FunSuite {

  test ("REST: valid Get method") {
    @ModelFree case class Person(
                                  @get id: Int,
                                  @get name: String,
                                  @get email: String,
                                  @get subscription: String
                                )

    """Person.Get(id = 42, name = "Bill", email = "some@exmaple.com", subscription = "medium.com")""" should compile
    """Person.Get(42, "Bill", "some@exmaple.com", "medium.com")""" should compile
    """Person.Get(42, name =  "Bill", "some@exmaple.com", "medium.com")""" should compile
  }

  test ("REST: not valid Get method") {
    @ModelFree case class Person(
                                  @get id: Int,
                                  @get name: String,
                                  @get email: String,
                                  @get subscription: String
                                )

    """Person.Get(id = 42, name = "Bill", email = "some@exmaple.com")""" shouldNot compile
    """Person.Get(id = 42, wrong = "Bill", email = "some@exmaple.com", subscription = "medium.com")""" shouldNot compile
    """Person.GET(id = 42, wrong = "Bill", email = "some@exmaple.com", subscription = "medium.com")""" shouldNot compile
  }

  test ("REST: valid Post method") {
    @ModelFree case class Person(
                                  id: Int,
                                  @post name: String,
                                  @post email: String,
                                  @post subscription: String
                                )

    """Person.Post(name = "Bill", email = "some@exmaple.com", subscription = "medium.com")""" should compile
    """Person.Post("Bill", "some@exmaple.com", "medium.com")""" should compile
    """Person.Post(name = "Bill", "some@exmaple.com", "medium.com")""" should compile
  }

  test ("REST: not valid Post method") {
    @ModelFree case class Person(
                                  id: Int,
                                  @post name: String,
                                  @post email: String,
                                  @post subscription: String
                                )

    """Person.Post(name = "Bill", email = "some@exmaple.com")""" shouldNot compile
    """Person.Post(id = 42, name = "Bill", email = "some@exmaple.com", subscription = "medium.com")""" shouldNot compile
    """Person.Post(wrong = "Bill", email = "some@exmaple.com", subscription = "medium.com")""" shouldNot compile
  }

  test ("REST: valid Put method") {
    @ModelFree case class Person(
                                  id: Int,
                                  @put name: String,
                                  @put email: String,
                                  subscription: String
                                )

    """Person.Put(name = "Bill", email = "some@exmaple.com")""" should compile
    """Person.Put("Bill", "some@exmaple.com")""" should compile
    """Person.Put(name = "Bill", "some@exmaple.com")""" should compile
  }

  test ("REST: not valid Put method") {
    @ModelFree case class Person(
                                  id: Int,
                                  @put name: String,
                                  @put email: String,
                                  subscription: String
                                )

    """Person.Put(name = "Bill")""" shouldNot compile
    """Person.Put(id = 42, name = "Bill", email = "some@exmaple.com")""" shouldNot compile
    """Person.Put(wrong = "some@exmaple.com", subscription = "medium.com")""" shouldNot compile
  }

  test ("REST: valid Delete method") {
    @ModelFree case class Person(
                                  @delete id: Int,
                                  name: String,
                                  email: String,
                                  subscription: String
                                )

    """Person.Delete(id = 1)""" should compile
    """Person.Delete(22)""" should compile
  }

  test ("REST: not valid Delete method") {
    @ModelFree case class Person(
                                  @delete id: Int,
                                  name: String,
                                  email: String,
                                  subscription: String
                                )

    """Person.Delete(id = 1, name = "Steve")""" shouldNot compile
    """Person.Delete(name = "Bill")""" shouldNot compile
  }

  test ("Complex REST suite") {
    @ModelFree case class Person(
                                  @get @delete id: Int,
                                  @get @post @put name: String,
                                  @get @post @put email: String,
                                  @get @post @put subscription: String
                                )

    """Person.Get(id = 42, name = "Bill", email = "some@exmaple.com", subscription = "medium.com")""" should compile
    """Person.Post(name = "Bill", email = "some@exmaple.com", subscription = "medium.com")""" should compile
    """Person.Put(name = "Bill", email = "some@exmaple.com", subscription = "medium.com")""" should compile
    """Person.Delete(id = 42)""" should compile
  }

  test ("Not valid complex REST suite: paramaters amount") {
    @ModelFree case class Person(
                                  @get @delete id: Int,
                                  @get @post @put name: String,
                                  @get @post @put email: String,
                                  @get @post @put subscription: String
                                )

    """Person.Get(id = 42, email = "some@exmaple.com", subscription = "medium.com")""" shouldNot compile
    """Person.Post(name = "Bill", email = "some@exmaple.com")""" shouldNot compile
    """Person.Put(name = "Bill", email = "some@exmaple.com")""" shouldNot compile
    """Person.Delete(id = 42, name = "Steve")""" shouldNot compile
  }

}
