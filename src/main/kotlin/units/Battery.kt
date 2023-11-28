package units

import constvalue.ConstValues
import org.kalasim.Component
import scheduler.TargetSetTask
import scheduler.TaskScheduler
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class Battery (
    batteryId: Int,
    target: Double,
    ratedAcPower: Double,
    constants: ConstValues,
    private var charge: Double,
    hasError: Boolean,
): AbstractUnit(
    id = batteryId,
    type = UnitType.BATTERY,
    ratedAcPower = ratedAcPower,
    constants = constants,
    taskScheduler = TaskScheduler(),
    targetOutput = target,
    hasError = hasError,
    lastReadPower = 0.0) {

    override fun repeatedProcess() = sequence<Component> {
        hold(1.minutes)
        taskScheduler.checkTasks()
        changeProsume()
        println("Battery $id: $charge, $now")
    }

    override fun readNewValue(): UnitPower {
        val unitPower = if(targetOutput == 0.0)
            UnitPower(id, 0.0, now, UnitPowerMessage.PRODUCE)
        else if(targetOutput < 0)
            UnitPower(id, constants.UP_POWER_CONTROL_PER_TICK, now, UnitPowerMessage.CONSUME)
        else
            UnitPower(id, constants.DOWN_POWER_CONTROL_PER_TICK, now, UnitPowerMessage.PRODUCE)
        lastReadTime = now
        lastReadPower = unitPower.power
        return unitPower
    }

    override fun readOldValue(): UnitPower {
        return if(targetOutput == 0.0)
            UnitPower(id, 0.0, now, UnitPowerMessage.PRODUCE)
        else if(targetOutput < 0)
            UnitPower(id, lastReadPower, now, UnitPowerMessage.CONSUME)
        else
            UnitPower(id, lastReadPower, now, UnitPowerMessage.PRODUCE)
    }

    override fun command(target: Double) {
        lastTargetCommand = target
        taskScheduler.addTask(TargetSetTask(this, constants.POWER_CONTROL_REACTION_TIME, target))
    }

    private fun changeProsume(){
        if(targetOutput > 0.0) {
            if (charge < ratedAcPower - constants.UP_POWER_CONTROL_PER_TICK)
                charge += constants.UP_POWER_CONTROL_PER_TICK
        } else if(targetOutput < 0.0){
            if(charge > constants.DOWN_POWER_CONTROL_PER_TICK)
                charge -= constants.DOWN_POWER_CONTROL_PER_TICK
        }
    }

    fun isFull(): Boolean {
        return charge > ratedAcPower * 0.8
    }
}