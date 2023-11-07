package units

import Config
import constvalue.ConstValues
import event.InverterEvent
import event.UnitReadEvent
import org.apache.commons.math3.distribution.UniformRealDistribution
import org.kalasim.*
import org.kalasim.misc.AmbiguousDuration
import scheduler.TargetSetTask
import scheduler.TaskScheduler
import util.eventLogging
import kotlin.math.floor
import kotlin.time.seconds


//inverter szintű logging ( inverterId, inverterPower!=inverterReadPower)
class Inverter(
    inverterId: Int,
    target: Double,
    constants: ConstValues,
    hasError: Boolean,
    private var prosume: Double = 0.0,
): AbstractUnit
    (id = inverterId,
    type = UnitType.INVERTER,
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
        lastReadPower = prosume
        val timeAccuracy = floor(constants.READ_FREQUENCY * constants.INVERTER_TIME_ACCURACY).toInt()
        randomReadTime = uniform(constants.READ_FREQUENCY - timeAccuracy, constants.READ_FREQUENCY + timeAccuracy)
        produceAccuracy = constants.RATED_AC_POWER * constants.INVERTER_PRODUCE_ACCURACY
    }


    override fun repeatedProcess() = sequence<Component> {
        hold(1)
        taskScheduler.checkTasks()
        changeProsume()
        //println("Inverter $id: $prosume, $targetOutput, $now")
    }

    override fun read(): UnitPower {
        val power = readingWithChecks()
        eventLogging(Config.UNIT_READ_LOG){log(UnitReadEvent(id, power.power, now))}
        return power
    }

    override fun command(target: Double) {
        require(targetOutput >= 0) {"Inverter $id TargetProsume can't be below 0."}
        setTargetProsume(target)
    }

    private fun changeProsume() {
        if (prosume != targetOutput) {
            if (targetOutput > prosume) {
                increaseProsume()
            } else {
                decreaseProsume()
            }
        }
        eventLogging(Config.UNIT_LOG) { log(InverterEvent(id, prosume, now)) }
    }


    private fun increaseProsume(){  //TODO jó-e?
        val newProsume = prosume + constants.UP_POWER_CONTROL_PER_TICK
        prosume = if (newProsume > targetOutput){
            targetOutput
        } else {
            newProsume
        }
    }

    private fun decreaseProsume(){
        val newProsume = prosume - constants.DOWN_POWER_CONTROL_PER_TICK
        prosume = if(newProsume < 0.0){
            0.0
        } else  if (newProsume < targetOutput){
            targetOutput
        } else {
            newProsume
        }
    }

    private fun readingWithChecks(): UnitPower {
        return if(!hasError) {
            if (now.minus(lastReadTime) > constants.READ_FREQUENCY) {
                lastReadTime = now
                lastReadPower = prosume
                prosume
                UnitPower(id, prosume, now, UnitPowerMessage.PRODUCE)
            } else {
                lastReadPower
                UnitPower(id, lastReadPower, lastReadTime, UnitPowerMessage.PRODUCE)
            }
        } else {
            UnitPower(id, 0.0, lastReadTime, UnitPowerMessage.ERROR)
        }
    }

    private fun setTargetProsume(target: Double) {
        if(floor(target) != floor(lastTargetCommand)) {
            val newTarget = if (target > constants.RATED_AC_POWER) {
                uniform(constants.RATED_AC_POWER.minus(produceAccuracy), constants.RATED_AC_POWER).invoke()
            } else {
                uniform(target - produceAccuracy, target + produceAccuracy).invoke()
            }
            lastTargetCommand = target
            val holdInInt = randomReadTime().toInt()
            taskScheduler.addTask(TargetSetTask(this, holdInInt, newTarget))
        }
    }


}