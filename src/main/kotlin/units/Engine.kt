package units

import constvalue.ConstValues
import event.EngineReadEvent
import event.ProducerReadEvent
import org.kalasim.invoke
import org.kalasim.uniform
import scheduler.EngineStartTask
import scheduler.TargetSetTask
import scheduler.TaskScheduler
import scheduler.TaskType
import util.eventLogging
import kotlin.time.Duration.Companion.minutes

class Engine(
    engineId: Int,
    private val minimumRunningPower: Double,
    ratedAcPower: Double,
    constants: ConstValues,
    targetOutput: Double,
    private var produce: Double = 0.0,
    private val heatUpTimeInTick: Int = 5,
    hasError: Boolean,
    var isStarted: Boolean = false,
) : AbstractUnit(
    id = engineId,
    type = UnitType.ENGINE,
    ratedAcPower = ratedAcPower,
    constants  = constants,
    taskScheduler = TaskScheduler(),
    targetOutput = targetOutput,
    hasError = hasError,
    lastReadPower = produce
) {

    private val produceAccuracy: Double = ratedAcPower * constants.PRODUCE_ACCURACY

    override fun repeatedProcess() = sequence{
        hold(1.minutes)
        taskScheduler.checkTasks()
        changeProduce()
        println("Engine $id: $produce, $lastTargetCommand, $now")
    }

    override fun read(loggingEnabled: Boolean): UnitPower{
        val power = super.read(loggingEnabled)
        eventLogging(loggingEnabled) {
            log(
                EngineReadEvent(
                    id,
                    type,
                    0,
                    ratedAcPower.toInt(),
                    power.power.toInt(),
                    lastTargetCommand.toInt(),
                    now
                )
            )
        }
        return power
    }

    override fun readNewValue(): UnitPower {
        lastReadTime = now
        lastReadPower = produce
        return UnitPower(id, produce, now, UnitPowerMessage.PRODUCE)
    }

    override fun readOldValue(): UnitPower {
        return UnitPower(id, lastReadPower, lastReadTime, UnitPowerMessage.PRODUCE)
    }


    override fun command(target: Double) { // not good if every 2 sec change
        if(target == 0.0 || target < minimumRunningPower){
            isStarted = false
            lastTargetCommand = 0.0
            taskScheduler.addTask(TargetSetTask(this, constants.POWER_CONTROL_REACTION_TIME, 0.0))
            val engineStartTask = taskScheduler.getTaskByType(TaskType.ENGINE_START).firstOrNull()
            engineStartTask?.let { taskScheduler.removeTask(it) }
        }else{
            if(!isStarted){
                isStarted = true
                val newTarget = getRandomizeTarget(target)
                lastTargetCommand = newTarget
                taskScheduler.addTask(EngineStartTask(this, heatUpTimeInTick, newTarget))
            } else {
                if (target !in targetOutput - produceAccuracy..targetOutput + produceAccuracy) {
                    val newTarget = getRandomizeTarget(target)
                    lastTargetCommand = newTarget
                    val engineStartTime = getEngineStartTime()
                    taskScheduler.addTask(TargetSetTask(this, engineStartTime + constants.POWER_CONTROL_REACTION_TIME, newTarget))
                }
            }
        }
    }

    private fun getEngineStartTime(): Int {
        val engineStartTasks = taskScheduler.getTaskByType(TaskType.ENGINE_START)
        if(engineStartTasks.size > 1){
            error("More then 1 EngineStartTasks")
        } else {
            val engineStartTask = engineStartTasks.firstOrNull()
            return engineStartTask?.holdInTick ?: 0
        }
    }

    fun getStartPower(): Double {
        return ratedAcPower.times(minimumRunningPower)
    }

    private fun getRandomizeTarget(target: Double): Double {
        val newTarget = uniform(target - produceAccuracy, target + produceAccuracy).invoke()
        return if (newTarget > ratedAcPower)
            ratedAcPower
        else
            newTarget
    }

    private fun changeProduce() {
        produce = targetOutput
    }

}