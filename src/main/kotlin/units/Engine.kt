package units

import constvalue.ConstValues
import org.kalasim.Component
import org.kalasim.invoke
import org.kalasim.uniform
import scheduler.TargetSetTask
import scheduler.TaskScheduler

class Engine(
    engineId: Int,
    private val minimumRunningPower: Double,
    ratedAcPower: Double,
    constants: ConstValues,
    targetOutput: Double,
    private var produce: Double = 0.0,
    private val heatUpTimeInTick: Int = 5,
    hasError: Boolean,
    private var isStarted: Boolean = false,
): AbstractUnit(engineId, UnitType.ENGINE, ratedAcPower, constants, TaskScheduler() ,targetOutput, hasError) {

    private val produceAccuracy: Double = ratedAcPower * constants.PRODUCE_ACCURACY


    override fun repeatedProcess() = sequence<Component> {
        hold(1)
        taskScheduler.checkTasks()
        println("Engine $id: $produce, $targetOutput, $now")
    }

    override fun read(): UnitPower {
        return UnitPower(id, produce, now, UnitPowerMessage.PRODUCE)
    }


    override fun command(target: Double) { // not good if every 2 sec change
        if(target == 0.0){
            isStarted = false
            targetOutput = 0.0
        }else{
            if(target < minimumRunningPower){
                isStarted = false
                targetOutput = 0.0
            }else{
                if(target !in targetOutput - produceAccuracy..targetOutput + produceAccuracy){
                    val newTarget = getRandomizeTarget(target)
                    taskScheduler.addTask(TargetSetTask(this, heatUpTimeInTick, newTarget))
                }
            }
        }
    }

    private fun getRandomizeTarget(target: Double): Double{
        val newTarget = uniform(target - produceAccuracy, target + produceAccuracy).invoke()
        return if (newTarget > ratedAcPower)
            ratedAcPower
        else
            newTarget
    }

}