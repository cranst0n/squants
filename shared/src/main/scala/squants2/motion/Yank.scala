/*                                                                      *\
** Squants                                                              **
**                                                                      **
** Scala Quantities and Units of Measure Library and DSL                **
** (c) 2013-2022, Gary Keorkunian, et al                                **
**                                                                      **
\*                                                                      */

package squants2.motion

import squants2._
import scala.math.Numeric.Implicits.infixNumericOps

final case class Yank[A: Numeric] private [squants2]  (value: A, unit: YankUnit)
  extends Quantity[A, Yank.type] {
  override type Q[B] = Yank[B]

  def toNewtonsPerSecond: A = to(NewtonsPerSecond)
}

object Yank extends Dimension("Yank") {

  override def primaryUnit: UnitOfMeasure[this.type] with PrimaryUnit = NewtonsPerSecond
  override def siUnit: UnitOfMeasure[this.type] with SiUnit = NewtonsPerSecond
  override lazy val units: Set[UnitOfMeasure[this.type]] = 
    Set(NewtonsPerSecond)

  implicit class YankCons[A](a: A)(implicit num: Numeric[A]) {
    def newtonsPerSecond: Yank[A] = NewtonsPerSecond(a)
  }

  lazy val newtonsPerSecond: Yank[Int] = NewtonsPerSecond(1)

  override def numeric[A: Numeric]: QuantityNumeric[A, this.type] = YankNumeric[A]()
  private case class YankNumeric[A: Numeric]() extends QuantityNumeric[A, this.type](this) {
    override def times(x: Quantity[A, Yank.type], y: Quantity[A, Yank.type]): Quantity[A, Yank.this.type] =
      NewtonsPerSecond(x.to(NewtonsPerSecond) * y.to(NewtonsPerSecond))
  }
}

abstract class YankUnit(val symbol: String, val conversionFactor: ConversionFactor) extends UnitOfMeasure[Yank.type] {
  override lazy val dimension: Yank.type = Yank
  override def apply[A: Numeric](value: A): Yank[A] = Yank(value, this)
}

case object NewtonsPerSecond extends YankUnit("N/s", 1.0) with PrimaryUnit with SiUnit