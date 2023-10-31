package units

import org.kalasim.Component

/**
 * power producing or consuming unit
 * @property id
 * @property type
 */
abstract class AbstractUnit(
    val id: Int,
    val type: UnitType
): Component() {

    abstract fun read(): UnitPower
    abstract fun command(target: Double)

}