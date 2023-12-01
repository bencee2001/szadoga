package units

import constvalue.ConstValues
import org.apache.commons.math3.distribution.UniformRealDistribution
import org.kalasim.Component
import org.kalasim.TickTime
import org.kalasim.invoke
import org.kalasim.uniform
import scheduler.TaskScheduler

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

    private val randomReadTime: UniformRealDistribution

    init {
        val timeAccuracy = constants.TIME_ACCURACY + 0.1
        val lowerBoundRead = constants.READ_FREQUENCY - timeAccuracy
        randomReadTime = uniform(
            if(lowerBoundRead >= 0) lowerBoundRead else 0.0,
            constants.READ_FREQUENCY + timeAccuracy
        )
    }

    open fun read(loggingEnabled: Boolean): UnitPower {
        return readingWithChecks()
    }

    private fun readingWithChecks(): UnitPower {
        return if (!hasError) {
            if (now.minus(lastReadTime) > randomReadTime.invoke().toInt())
                readNewValue()
            else
                readOldValue()
        } else
            UnitPower(id, 0.0, lastReadTime, UnitPowerMessage.ERROR)
    }

    protected abstract fun readNewValue(): UnitPower
    protected abstract fun readOldValue(): UnitPower

    abstract fun command(target: Double)

}