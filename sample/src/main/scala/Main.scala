import com.github.model4s.Converter.Mappable
import com.github.model4s.{Converter, MapClassPair}

/**
  * Showcases of API usage
  */
object Main extends App {
  case class User(id: Int, name: String)
  def mapify[T: MapClassPair](t: T) = implicitly[MapClassPair[T]].toMap(t)
  def materialize[T: MapClassPair](map: Mappable) = implicitly[MapClassPair[T]].fromMap(map)

  val samplePerson = User(13, "Steve")
  val sampleMap = Map("id" -> 15, "name" -> "Bill")
  mapify(samplePerson) foreach println
  println(materialize[User](sampleMap))

  //Bijection
  println("toMap - "+Converter.transform[User].apply(samplePerson))
  println("fromMap - "+Converter.transform[User].invert(sampleMap))
}
