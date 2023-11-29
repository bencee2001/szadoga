package units

import constvalue.ConstValues
import event.BatteryReadEvent
import org.kalasim.Component
import scheduler.TargetSetTask
import scheduler.TaskScheduler
import util.eventLogging
import kotlin.time.Duration.Companion.minutes

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
        println("Battery $id: $charge, $targetOutput, $now")
    }

    override fun read(loggingEnabled: Boolean): UnitPower {
        val power = super.read(loggingEnabled)
        eventLogging(loggingEnabled){
            log(
                BatteryReadEvent(
                    id = id,
                    minPower = -constants.DOWN_POWER_CONTROL_PER_TICK.toInt(),
                    maxPower = constants.UP_POWER_CONTROL_PER_TICK.toInt(),
                    minCharge = 0,
                    maxCharge = ratedAcPower.toInt(),
                    charge = charge.toInt(),
                    prosume = getPowerForEvent(power),
                    currentTarget = targetOutput.toInt(),
                    time = now
                )
            )
        }
        return power
    }

    private fun getPowerForEvent(power: UnitPower): Int{
        return when (power.unitPowerMessage) {
            UnitPowerMessage.PRODUCE -> power.power.toInt()
            UnitPowerMessage.CONSUME -> -power.power.toInt()
            else -> 0
        }
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

    fun isEmpty(): Boolean {
        return charge < ratedAcPower * 0.1
    }

    fun isFull(): Boolean {
        return charge > ratedAcPower * 0.85
    }
}