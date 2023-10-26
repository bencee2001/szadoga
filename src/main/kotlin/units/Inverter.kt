package units

import constvalue.inverter.InverterConst
import event.InverterEvent
import org.kalasim.Component
import org.kalasim.TickTime
import util.unitEventLogging


//inverter szintű logging ( inverterId, inverterPower!=inverterReadPower)
class Inverter(
    inverterId: Int,
    private var prosume: Float,
    private var targetProsume: Float,
    private var lastReadTime: TickTime,
    private var isReadable: Boolean,
    private val constValues: InverterConst
): AbstractUnit(inverterId, type = UnitType.INVERTER) {

    private var lastReadPower: Float

    init{
        lastReadPower = prosume
    }


    override fun repeatedProcess() = sequence<Component> {
        hold(1)
        changeProsume()
        println("Inside: $prosume")
    }

    override fun read(): Float {
        return readingWithChecks()
    }

    override  fun command(target: Float) {
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


    private fun increaseProsume(change: Float){  //TODO jó-e?
        val newProsume = prosume + change
        prosume = if(newProsume > constValues.MAX_POWER_OUTPUT){
            constValues.MAX_POWER_OUTPUT
        } else  if (newProsume > targetProsume){
            targetProsume
        } else {
            newProsume
        }
    }

    private fun decreaseProsume(change: Float){
        val newProsume = prosume - change
        prosume = if(newProsume < 0F){
            0F
        } else  if (newProsume < targetProsume){
            targetProsume
        } else {
            newProsume
        }
    }

    private fun readingWithChecks(): Float {
        return if(isReadable) {
            if (now.minus(lastReadTime) > constValues.READ_FREQUENCY) {
                lastReadTime = now
                lastReadPower = prosume
                prosume
            } else {
                lastReadPower
            }
        } else {
            0F
        }
    }

    private fun setTargetProsume(target: Float) {
        targetProsume = if (target > constValues.MAX_POWER_OUTPUT) {
            logger.warn { "TargetProsume bigger than Inverter $id maximum power output." }
            constValues.MAX_POWER_OUTPUT
        } else {
            target
        }
    }
}