import com.github.model4s.cmap.Mappable.CaseClassMap
import com.github.model4s.cmap.{Mappable, MapClassPair}

/**
  * Showcases of API usage
  */
object CMapFree extends App {

  case class User(id: Int, name: String)

  def mapify[T: MapClassPair](t: T) = implicitly[MapClassPair[T]].toMap(t)

  def materialize[T: MapClassPair](map: CaseClassMap) = implicitly[MapClassPair[T]].fromMap(map)

  val samplePerson = User(13, "Steve")
  val sampleMap = Map("id" -> 15, "name" -> "Bill")

  val personMap: Map[String, Any] = mapify(samplePerson)
  personMap foreach println

  println(materialize[User](sampleMap))

  //Bijection
  val pojoToMap: Map[String, Any] = Mappable.transform[User].apply(samplePerson)
  val mapToPojo: User = Mappable.transform[User].invert(sampleMap)

  println(s"fromMap - $mapToPojo")
  println(s"toMap - $pojoToMap")

}
