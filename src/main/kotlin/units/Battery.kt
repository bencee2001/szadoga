package units

import constvalue.ConstValues
import org.kalasim.Component
import scheduler.TargetSetTask
import scheduler.TaskScheduler

class Battery (
    batteryId: Int,
    target: Double,
    constants: ConstValues,
    private var charge: Double,
    hasError: Boolean,
): AbstractUnit(batteryId, UnitType.BATTERY, constants, TaskScheduler(), target, hasError) {

    override fun repeatedProcess() = sequence<Component> {
        hold(1)
        taskScheduler.checkTasks()
        changeProsume()
        println("Battery $id: $charge, $now")
    }


    override fun read(): UnitPower {
        return if(targetOutput == 0.0)
            UnitPower(id, 0.0, now, UnitPowerMessage.PRODUCE)
        else if(targetOutput < 0)
            UnitPower(id, constants.UP_POWER_CONTROL_PER_TICK, now, UnitPowerMessage.CONSUME)
        else
            UnitPower(id, constants.DOWN_POWER_CONTROL_PER_TICK, now, UnitPowerMessage.PRODUCE)
    }

    override fun command(target: Double) {
        taskScheduler.addTask(TargetSetTask(this, constants.POWER_CONTROL_REACTION_TIME, target))
    }

    private fun changeProsume(){
        if(targetOutput > 0.0) {
            if (charge < constants.RATED_AC_POWER - constants.UP_POWER_CONTROL_PER_TICK)
                charge += constants.UP_POWER_CONTROL_PER_TICK
        } else if(targetOutput < 0.0){
            if(charge > constants.DOWN_POWER_CONTROL_PER_TICK)
                charge -= constants.DOWN_POWER_CONTROL_PER_TICK
        }
    }

    fun isFull(): Boolean {
        return charge > constants.RATED_AC_POWER * 0.8
    }
}