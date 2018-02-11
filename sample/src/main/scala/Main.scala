import com.github.model4s.MapClassPair

/**
  * Showcases of API usage
  */
object Main extends App {
  case class User(id: Int, name: String)
  def mapify[T: MapClassPair](t: T) = implicitly[MapClassPair[T]].toMap(t)
  mapify(User(11, "Steve")) foreach println
}
