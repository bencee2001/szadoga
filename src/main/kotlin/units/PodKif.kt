package units

import constvalue.ConstValues
import org.kalasim.Component
import scheduler.TargetSetTask
import scheduler.TaskScheduler

class PodKif(
    podKifId: Int,
    ratedAcPower: Double,
    constants: ConstValues,
    startTargetOutput: Double = 0.0,
    private var isProducing: Boolean,
    private val startDelay: Int,
    hasError: Boolean
): AbstractUnit(podKifId, UnitType.KIF_POD, ratedAcPower, constants, TaskScheduler(), startTargetOutput, hasError) {

    override fun repeatedProcess() = sequence<Component> {
        hold(1)
        taskScheduler.checkTasks()
        checkProducing()
    }

    override fun read(): UnitPower {
        return if (isProducing){
            UnitPower(id, ratedAcPower, now, UnitPowerMessage.PRODUCE)
        } else {
            UnitPower(id, 0.0, now, UnitPowerMessage.PRODUCE)
        }
    }

    override fun command(target: Double) {
        if(target != 0.0)
            taskScheduler.addTask(TargetSetTask(this, startDelay, target))
        else
            taskScheduler.addTask(TargetSetTask(this, startDelay, 0.0))
    }

    private fun checkProducing(){
        isProducing = targetOutput != 0.0
    }
}