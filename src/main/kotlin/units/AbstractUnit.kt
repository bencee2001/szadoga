package units

import constvalue.ConstValues
import org.kalasim.Component
import scheduler.TaskScheduler

/**
 * power producing or consuming unit
 * @property id
 * @property type
 */
abstract class AbstractUnit(
    val id: Int,
    val type: UnitType,
    val constants: ConstValues,
    val taskScheduler: TaskScheduler,
    var targetOutput: Double,
    var hasError: Boolean,
): Component() {

    abstract fun read(): UnitPower
    abstract fun command(target: Double)

}