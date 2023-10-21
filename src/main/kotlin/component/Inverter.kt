package component

import event.InverterEvent
import model.ComponentType
import org.kalasim.Component
import org.kalasim.TickTime
import util.eventLogging

class Inverter(
    private val inverterId: Int,
    private var prosume: Float,
    private var targetProsume: Float,
    private val maxPowerOutput: Float,
    private val powerControlPerTick: Float,
    private val readFrequency: Int,
    private var lastReadTime: TickTime,
    private var lastReadPower: Float
): AbstractComponent(inverterId, type = ComponentType.INVERTER) {


    override fun repeatedProcess() = sequence<Component> {
        hold(1)
        if(targetProsume > prosume){
            increaseProsume(powerControlPerTick)
        } else {
            decreaseProsume(powerControlPerTick)
        }
        eventLogging { log(InverterEvent(inverterId, prosume, now)) }
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


    override fun read(): Float {
        return if(now.minus(lastReadTime) > readFrequency){
            lastReadTime = now
            lastReadPower = prosume
            prosume
        }else {
            lastReadPower
        }
    }

    override fun command(target: Float) {
        require(targetProsume >= 0) {"Inverter $inverterId TargetProsume can't be below 0."}
        setTargetProsume(target)
    }

    private fun setTargetProsume(target: Float) {
        targetProsume = if (target > maxPowerOutput) {
            logger.warn { "TargetProsume bigger than Inverter $inverterId maximum power output." }
            maxPowerOutput
        } else {
            target
        }
    }
}