/*                                                                      *\
** Squants                                                              **
**                                                                      **
** Scala Quantities and Units of Measure Library and DSL                **
** (c) 2013-2022, Gary Keorkunian, et al                                **
**                                                                      **
\*                                                                      */

package squants2.time

import squants2._
import scala.math.Numeric.Implicits.infixNumericOps

final case class Time[A: Numeric] private [squants2]  (value: A, unit: TimeUnit)
  extends Quantity[A, Time.type] {
  override type Q[B] = Time[B]

  // BEGIN CUSTOM OPS
  // END CUSTOM OPS

  def toNanoseconds: A = to(Nanoseconds)
  def toMicroseconds: A = to(Microseconds)
  def toMilliseconds: A = to(Milliseconds)
  def toSeconds: A = to(Seconds)
  def toMinutes: A = to(Minutes)
  def toHours: A = to(Hours)
  def toDays: A = to(Days)
}

object Time extends BaseDimension("Time", "T") {

  override def primaryUnit: UnitOfMeasure[this.type] with PrimaryUnit = Milliseconds
  override def siUnit: UnitOfMeasure[this.type] with SiBaseUnit = Seconds
  override lazy val units: Set[UnitOfMeasure[this.type]] = 
    Set(Nanoseconds, Microseconds, Milliseconds, Seconds, Minutes, Hours, Days)

  implicit class TimeCons[A](a: A)(implicit num: Numeric[A]) {
    def nanoseconds: Time[A] = Nanoseconds(a)
    def microseconds: Time[A] = Microseconds(a)
    def milliseconds: Time[A] = Milliseconds(a)
    def seconds: Time[A] = Seconds(a)
    def minutes: Time[A] = Minutes(a)
    def hours: Time[A] = Hours(a)
    def days: Time[A] = Days(a)
  }

  lazy val nanoseconds: Time[Int] = Nanoseconds(1)
  lazy val microseconds: Time[Int] = Microseconds(1)
  lazy val milliseconds: Time[Int] = Milliseconds(1)
  lazy val seconds: Time[Int] = Seconds(1)
  lazy val minutes: Time[Int] = Minutes(1)
  lazy val hours: Time[Int] = Hours(1)
  lazy val days: Time[Int] = Days(1)

  override def numeric[A: Numeric]: QuantityNumeric[A, this.type] = TimeNumeric[A]()
  private case class TimeNumeric[A: Numeric]() extends QuantityNumeric[A, this.type](this) {
    override def times(x: Quantity[A, Time.type], y: Quantity[A, Time.type]): Quantity[A, Time.this.type] =
      Milliseconds(x.to(Milliseconds) * y.to(Milliseconds))
  }
}

abstract class TimeUnit(val symbol: String, val conversionFactor: ConversionFactor) extends UnitOfMeasure[Time.type] {
  override lazy val dimension: Time.type = Time
  override def apply[A: Numeric](value: A): Time[A] = Time(value, this)
}

case object Nanoseconds extends TimeUnit("ns", 1.0E-6) with SiUnit
case object Microseconds extends TimeUnit("µs", 0.001) with SiUnit
case object Milliseconds extends TimeUnit("ms", 1) with PrimaryUnit with SiUnit
case object Seconds extends TimeUnit("s", 1000.0) with SiBaseUnit
case object Minutes extends TimeUnit("min", 60000.0)
case object Hours extends TimeUnit("h", 3600000.0)
case object Days extends TimeUnit("d", 8.64E7)

case object PlankTime extends TimeUnit("tp", 5.39E-44)
case object Picoseconds extends TimeUnit("ps", MetricSystem.Pico)
case object Weeks extends TimeUnit("w", 7 * Days.conversionFactor)
