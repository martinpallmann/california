package california

import nanotest.TestSuite
import java.net.URI
import java.time.{LocalDate, LocalDateTime, LocalTime, ZoneOffset}

object Tests extends TestSuite {

  def compare[A](ct: CalType[A], value: A, expected: String) = {
    def format(a: A): String = ct.format(a)
    assert(expected == format(value))
  }

  test("format boolean (false)") {
    compare(boolCanCalType, false, "FALSE")
  }

  test("format boolean (true)") {
    compare(boolCanCalType, true, "TRUE")
  }

  test("format date") {
    compare(localDateCanCalType, LocalDate.of(2001, 12, 24), "20011224")
  }

  test("format time") {
    compare(localTimeCanCalType, LocalTime.of(16, 35, 17), "163517")
  }

  test("format date-time") {
    compare(localDateTimeCanCalType, LocalDateTime.of(2001, 12, 24, 16, 35, 17), "20011224T163517")
  }

  test("format integer") {
    compare(intCanCalType, -5, "-5")
  }

  test("format uri") {
    compare(uriCanCalType, URI.create("http://www.example.com"), "http://www.example.com")
  }

  test("format zoneOffset +0300") {
    compare(zoneOffsetCanCalType, ZoneOffset.ofHours(3), "+0300")
  }

  test("format zoneOffset -023515") {
    compare(zoneOffsetCanCalType, ZoneOffset.ofHoursMinutesSeconds(-2, -35, -15), "-023515")
  }

  test("format zoneOffset +1237") {
    compare(zoneOffsetCanCalType, ZoneOffset.ofHoursMinutesSeconds(12, 37, 0), "+1237")
  }
}
