package units

import constvalue.ConstValues
import org.kalasim.Component
import org.kalasim.TickTime
import scheduler.TaskScheduler

/**
 * power producing or consuming unit
 * @property id
 * @property type
 */
abstract class AbstractUnit(
    val id: Int,
    val type: UnitType,
    val ratedAcPower: Double,
    val constants: ConstValues,
    val taskScheduler: TaskScheduler,
    var targetOutput: Double,
    var hasError: Boolean,
    var lastReadPower: Double,
    var lastReadTime: TickTime = TickTime(0),
    var lastTargetCommand: Double = 0.0
) : Component() {

    open fun read(): UnitPower {
        return readingWithChecks()
    }

    private fun readingWithChecks(): UnitPower {
        return if (!hasError) {
            if (now.minus(lastReadTime) > constants.READ_FREQUENCY)
                readNewValue()
            else
                readOldValue()
        } else
            UnitPower(id, 0.0, lastReadTime, UnitPowerMessage.ERROR)
    }

    abstract fun readNewValue(): UnitPower
    abstract fun readOldValue(): UnitPower

    abstract fun command(target: Double)

}