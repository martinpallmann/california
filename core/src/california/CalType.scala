package california

trait CalType[A] {
  def name: String
  def format(a: A): String
}

object CalType {
  def apply[A](implicit ct: CalType[A]): CalType[A] = ct
}
