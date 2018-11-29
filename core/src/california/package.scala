import java.net.URI
import java.time.{LocalDate, LocalDateTime, LocalTime, ZoneOffset}
import java.time.format.DateTimeFormatter

package object california {

  implicit class CalTypeOps[A: CalType](a: A) {
    def calType: String = CalType[A].format(a)
    def calName: String = CalType[A].name
  }

  /**
    * see https://tools.ietf.org/html/rfc5545#section-3.3.2
    */
  implicit val boolCanCalType: CalType[Boolean] = new CalType[Boolean] {
    val name: String = "BOOLEAN"
    def format(a: Boolean): String = if (a) "TRUE" else "FALSE"
  }

  /**
    * see https://tools.ietf.org/html/rfc5545#section-3.3.4
    */
  implicit val localDateCanCalType: CalType[LocalDate] = new CalType[LocalDate] {
    val name: String = "DATE"
    def format(a: LocalDate): String = a.format(DateTimeFormatter.BASIC_ISO_DATE)
  }

  /**
    * see https://tools.ietf.org/html/rfc5545#section-3.3.5
    */
  implicit val localDateTimeCanCalType: CalType[LocalDateTime] = new CalType[LocalDateTime] {
    val fmt = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
    val name: String = "DATE-TIME"
    def format(a: LocalDateTime): String = a.format(fmt)
  }

  /**
    * see https://tools.ietf.org/html/rfc5545#section-3.3.8
    */
  implicit val intCanCalType: CalType[Int] = new CalType[Int] {
    val name: String = "INTEGER"
    def format(a: Int): String = a.toString
  }

  /**
    * see https://tools.ietf.org/html/rfc5545#section-3.3.12
    */
  implicit val localTimeCanCalType: CalType[LocalTime] = new CalType[LocalTime] {
    val fmt = DateTimeFormatter.ofPattern("HHmmss")
    val name: String = "TIME"
    def format(a: LocalTime): String = a.format(fmt)
  }

  /**
    * see https://tools.ietf.org/html/rfc5545#section-3.3.13
    */
  implicit val uriCanCalType: CalType[URI] = new CalType[URI] {
    val name: String = "URI"
    def format(a: URI): String = a.toString
  }

  /**
    * see https://tools.ietf.org/html/rfc5545#section-3.3.14
    */
  implicit val zoneOffsetCanCalType: CalType[ZoneOffset] = new CalType[ZoneOffset] {
    val name: String = "UTC-OFFSET"
    def format(a: ZoneOffset): String = a.getId.replace(":", "")
  }
}
