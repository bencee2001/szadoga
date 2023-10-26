package units

import org.kalasim.Component

/**
 * power producing or consuming unit
 * @property id
 * @property type
 */
abstract class AbstractUnit(
    protected val id: Int,
    protected val type: UnitType
): Component() {

    abstract fun read(): Float
    abstract fun command(target: Float)

}