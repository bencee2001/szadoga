package units

import constvalue.ConstValues
import event.InverterReadEvent
import org.apache.commons.math3.distribution.UniformRealDistribution
import org.kalasim.*
import scheduler.tasks.TargetSetTask
import scheduler.TaskScheduler
import util.eventLogging
import kotlin.time.Duration.Companion.minutes

class Inverter(
    inverterId: Int,
    ratedAcPower: Double,
    target: Double,
    constants: ConstValues,
    hasError: Boolean,
    private var produce: Double = 0.0,
) : AbstractUnit
    (
    id = inverterId,
    type = UnitType.INVERTER,
    ratedAcPower = ratedAcPower,
    constants = constants,
    taskScheduler = TaskScheduler(),
    targetOutput = target,
    hasError = hasError,
    lastReadPower = produce
) {
    private val randomControlTime: UniformRealDistribution
    private val produceAccuracy: Double

    init {
        lastReadPower = produce
        val timeAccuracy = constants.TIME_ACCURACY + 0.1
        randomControlTime = uniform(
            constants.POWER_CONTROL_REACTION_TIME,
            constants.POWER_CONTROL_REACTION_TIME + timeAccuracy
        )
        produceAccuracy = ratedAcPower * constants.TARGET_ACCURACY + 0.1
    }


    override fun repeatedProcess() = sequence {
        hold(1.minutes)
        taskScheduler.checkTasks()
        changeProduce()
        println("Inverter $id: $produce, $targetOutput, $lastTargetCommand, $now")
    }

    override fun read(loggingEnabled: Boolean): UnitPower {
        val power = super.read(loggingEnabled)
        eventLogging(loggingEnabled) {
            log(
                InverterReadEvent(
                    id,
                    type,
                    0,
                    ratedAcPower.toInt(),
                    power.power.toInt(),
                    lastTargetCommand.toInt(),
                    power.unitPowerMessage,
                    now
                )
            )
        }
        return power
    }

    override fun readNewValue(): UnitPower {
        lastReadTime = now
        lastReadPower = produce
        produce
        return UnitPower(id, produce, now, UnitPowerMessage.PRODUCE)
    }

    override fun readOldValue(): UnitPower {
        return UnitPower(id, lastReadPower, lastReadTime, UnitPowerMessage.PRODUCE)
    }

    override fun command(target: Double) {
        setTargetProduce(target)
    }

    private fun changeProduce() {
        if (produce != targetOutput) {
            if (targetOutput > produce) {
                increaseProduce()
            } else {
                decreaseProduce()
            }
        }
    }

    private fun increaseProduce() {
        val newProduce = produce + constants.UP_POWER_CONTROL_PER_TICK
        produce = if (newProduce > targetOutput) {
            targetOutput
        } else {
            newProduce
        }
    }

    private fun decreaseProduce() {
        val newProsume = produce - constants.DOWN_POWER_CONTROL_PER_TICK
        produce = if (newProsume < targetOutput) {
            targetOutput
        } else {
            if (newProsume < 0.0)
                0.0
            else
                newProsume
        }
    }

    private fun setTargetProduce(target: Double) {
        if (target !in lastTargetCommand - produceAccuracy..lastTargetCommand + produceAccuracy) {
            val newTarget = getNewTarget(target)
            lastTargetCommand = target
            val holdInInt = randomControlTime().toInt()
            taskScheduler.addTask(TargetSetTask(this, holdInInt, newTarget))
        }
    }

    private fun getNewTarget(target: Double): Double {
        val newTarget = uniform(target - produceAccuracy, target + produceAccuracy).invoke()
        return if (newTarget > target)
            target
        else if (newTarget < 0.0)
            0.0
        else
            newTarget

    }


}