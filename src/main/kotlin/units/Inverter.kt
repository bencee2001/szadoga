package units

import constvalue.inverter.InverterConst
import event.InverterEvent
import org.apache.commons.math3.distribution.UniformRealDistribution
import org.kalasim.Component
import org.kalasim.TickTime
import org.kalasim.invoke
import org.kalasim.uniform
import util.unitEventLogging
import kotlin.math.floor


//inverter szintű logging ( inverterId, inverterPower!=inverterReadPower)
class Inverter(
    inverterId: Int,
    private var prosume: Double,
    private var targetProsume: Double,
    private var lastReadTime: TickTime,
    private var isReadable: Boolean,
    val maxAllowedAcPower: Double,
    val constValues: InverterConst
): AbstractUnit(inverterId, type = UnitType.INVERTER) {

    private var lastReadPower: Double
    private var holdForTarget: Int
    private var newTarget: Double
    private val randomReadTime: UniformRealDistribution
    private val produceAccuracy: Double


    init{
        lastReadPower = prosume
        holdForTarget = 0
        newTarget = targetProsume
        val timeAccuracy = floor(constValues.READ_FREQUENCY * constValues.INVERTER_TIME_ACCURACY).toInt()
        randomReadTime = uniform(constValues.READ_FREQUENCY - timeAccuracy, constValues.READ_FREQUENCY + timeAccuracy)
        produceAccuracy = maxAllowedAcPower * constValues.INVERTER_PRODUCE_ACCURACY
    }


    override fun repeatedProcess() = sequence<Component> {
        hold(1)
        setNewTarget()
        changeProsume()
        println("Inside $id: $prosume, $targetProsume, $newTarget, $holdForTarget")
    }

    private fun setNewTarget() {
       if(newTarget != targetProsume){
           holdForTarget -= 1
           if(holdForTarget == 0){
               targetProsume = newTarget
           }
       }
    }

    override fun read(): UnitPower {
        return readingWithChecks()
    }

    override fun command(target: Double) {
        require(targetProsume >= 0) {"Inverter $id TargetProsume can't be below 0."}
        setTargetProsume(target)
    }

    private fun changeProsume() {
        if (prosume != targetProsume) {
            if (targetProsume > prosume) {
                increaseProsume(constValues.POWER_CONTROL_PER_TICK)
            } else {
                decreaseProsume(constValues.POWER_CONTROL_PER_TICK)
            }
        }
        unitEventLogging { log(InverterEvent(id, prosume, now)) }
    }


    private fun increaseProsume(change: Double){  //TODO jó-e?
        val newProsume = prosume + change
        prosume = if (newProsume > targetProsume){
            targetProsume
        } else {
            newProsume
        }
    }

    private fun decreaseProsume(change: Double){
        val newProsume = prosume - change
        prosume = if(newProsume < 0.0){
            0.0
        } else  if (newProsume < targetProsume){
            targetProsume
        } else {
            newProsume
        }
    }

    private fun readingWithChecks(): UnitPower {
        return if(isReadable) {
            if (now.minus(lastReadTime) > constValues.READ_FREQUENCY) {
                lastReadTime = now
                lastReadPower = prosume
                prosume
                UnitPower(id, prosume, now, UnitPowerMessage.PRODUCE)
            } else {
                lastReadPower
                UnitPower(id, lastReadPower, lastReadTime, UnitPowerMessage.PRODUCE)
            }
        } else {
            UnitPower(id, lastReadPower, lastReadTime, UnitPowerMessage.NONE)
        }
    }

    private fun setTargetProsume(target: Double) {
        newTarget = if (target > maxAllowedAcPower) {
            logger.warn { "TargetProsume bigger than Inverter $id maximum power output." }
            uniform(maxAllowedAcPower.minus(produceAccuracy), maxAllowedAcPower).invoke()
        } else {
            uniform(target - produceAccuracy, target + produceAccuracy).invoke()
        }
        holdForTarget = randomReadTime().toInt()
    }


}