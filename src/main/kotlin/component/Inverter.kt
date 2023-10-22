package component

import event.InverterEvent
import model.ComponentType
import org.kalasim.Component
import org.kalasim.TickTime
import util.unitEventLogging

class Inverter(
    inverterId: Int,
    private var prosume: Float,
    private var targetProsume: Float,
    private val maxPowerOutput: Float,
    private val powerControlPerTick: Float,
    private val readFrequency: Int,
    private val powerControlReactionTime: Int,  //TODO használni
    private var lastReadTime: TickTime,
    private var isReadable: Boolean
): AbstractComponent(inverterId, type = ComponentType.INVERTER) {

    private var lastReadPower: Float

    init{
        lastReadPower = prosume
    }


    override fun repeatedProcess() = sequence<Component> {
        hold(1)
        changeProsume()
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
                increaseProsume(powerControlPerTick)
            } else {
                decreaseProsume(powerControlPerTick)
            }
        }
        unitEventLogging { log(InverterEvent(id, prosume, now)) }
    }


    private fun increaseProsume(change: Float){  //TODO jó-e?
        val newProsume = prosume + change
        prosume = if(newProsume > maxPowerOutput){
            maxPowerOutput
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
            if (now.minus(lastReadTime) > readFrequency) {
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
        targetProsume = if (target > maxPowerOutput) {
            logger.warn { "TargetProsume bigger than Inverter $id maximum power output." }
            maxPowerOutput
        } else {
            target
        }
    }
}