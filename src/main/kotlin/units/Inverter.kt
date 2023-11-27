package units

import LogFlags
import constvalue.ConstValues
import event.UnitReadEvent
import org.apache.commons.math3.distribution.UniformRealDistribution
import org.kalasim.*
import scheduler.TargetSetTask
import scheduler.TaskScheduler
import util.eventLogging
import kotlin.math.floor

class Inverter(
    inverterId: Int,
    ratedAcPower: Double,
    target: Double,
    constants: ConstValues,
    hasError: Boolean,
    private var produce: Double = 0.0,
): AbstractUnit
    (id = inverterId,
    type = UnitType.INVERTER,
    ratedAcPower = ratedAcPower,
    constants = constants,
    taskScheduler = TaskScheduler(),
    targetOutput = target,
    hasError = hasError) {

    private val randomReadTime: UniformRealDistribution
    private val produceAccuracy: Double
    private var lastReadPower: Double
    private var lastReadTime: TickTime = TickTime(0)
    private var lastTargetCommand: Double = 0.0

    init{
        lastReadPower = produce
        val timeAccuracy = constants.READ_FREQUENCY * constants.TIME_ACCURACY + 0.1
        println("timeAccuracy: ${constants.READ_FREQUENCY}----------------$timeAccuracy")
        randomReadTime = uniform(constants.READ_FREQUENCY - timeAccuracy, constants.READ_FREQUENCY + timeAccuracy)
        produceAccuracy = ratedAcPower * constants.PRODUCE_ACCURACY + 0.1
    }


    override fun repeatedProcess() = sequence<Component> {
        hold(1)
        taskScheduler.checkTasks()
        changeProsume()
        eventLogging(LogFlags.UNIT_READ_LOG){ log(UnitReadEvent(id, type, 0, ratedAcPower.toInt(), produce.toInt(), lastTargetCommand.toInt(),  now)) }

        //println("Inverter $id: $prosume, $targetOutput, $now")
    }

    override fun read(): UnitPower {
        val power = readingWithChecks()
        return power
    }

    override fun command(target: Double) {
        //require(targetOutput >= 0) {"Inverter $id TargetProsume can't be below 0."}
        setTargetProsume(target)
    }

    private fun changeProsume() {
        if (produce != targetOutput) {
            if (targetOutput > produce) {
                increaseProsume()
            } else {
                decreaseProsume()
            }
        }
    }


    private fun increaseProsume(){  //TODO jó-e?
        val newProsume = produce + constants.UP_POWER_CONTROL_PER_TICK
        produce = if (newProsume > targetOutput){
            targetOutput
        } else {
            newProsume
        }
    }

    private fun decreaseProsume(){
        val newProsume = produce - constants.DOWN_POWER_CONTROL_PER_TICK
        produce = if (newProsume < targetOutput){
            targetOutput
        } else {
            if(newProsume < 0.0)
                0.0
            else
                newProsume
        }
    }

    private fun readingWithChecks(): UnitPower {
        return if(!hasError) {
            if (now.minus(lastReadTime) > constants.READ_FREQUENCY) {
                lastReadTime = now
                lastReadPower = produce
                produce
                UnitPower(id, produce, now, UnitPowerMessage.PRODUCE)
            } else {
                lastReadPower
                UnitPower(id, lastReadPower, lastReadTime, UnitPowerMessage.PRODUCE)
            }
        } else {
            UnitPower(id, 0.0, lastReadTime, UnitPowerMessage.ERROR)
        }
    }

    private fun setTargetProsume(target: Double) {
        if(floor(target) != floor(lastTargetCommand)) {  //TODO normális range vizsgálat
            val newTarget = getNewTarget(target)
            lastTargetCommand = target
            val holdInInt = randomReadTime().toInt()
            println("holdInInt: $holdInInt")
            taskScheduler.addTask(TargetSetTask(this, holdInInt, newTarget))
        }
    }

    private fun getNewTarget(target: Double): Double{
        val newTarget = uniform(target - produceAccuracy, target + produceAccuracy).invoke()
        return if(newTarget > target)
            target
        else if(newTarget < 0.0)
            0.0
        else
            newTarget

    }


}