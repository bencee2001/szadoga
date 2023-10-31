package units

import org.kalasim.Component

/**
 * TODO
 *
 * @property prosume
 * @property ratedAcPower
 * @property warmingPerTick - positive double
 * @property coolingPerTick - positive double
 * @property powerStepW
 * @property temp
 * @property target
 * @property running
 * @constructor
 * TODO
 *
 * @param loadbankId
 */
class Loadbank(
    loadbankId: Int,
    private var prosume: Double = 0.0,
    private val ratedAcPower: Double,
    private val warmingPerTick: Double,
    private val coolingPerTick: Double,
    private val powerStepW: Double,
    private var temp: Double = 0.0,
    private var tempTarget: Double,
    private var running: Boolean = false,
    //TODO kell-e default heat
): AbstractUnit(loadbankId, type = UnitType.LOADBANK)  {

    private val maxTemp: Double = ratedAcPower.div(powerStepW)

    override fun repeatedProcess() = sequence<Component> {
        changeTemperature()
    }

    override fun read(): UnitPower {
         return UnitPower(id, temp * powerStepW, now, UnitPowerMessage.CONSUME)
    }

    override fun command(target: Double) {
        tempTarget = target.div(powerStepW)
        running = target != 0.0
    }

    fun canNextStart(): Boolean{
        return temp.div(maxTemp) > 0.75
    }

    private fun changeTemperature() {
        if(running){
            val newTemp = temp + warmingPerTick
            temp = if(newTemp > maxTemp){
                maxTemp
            } else {
                newTemp
            }
        } else {
            val newTemp = temp - coolingPerTick
            temp = if(newTemp < 0.0){
                maxTemp
            } else {
                newTemp
            }
        }
    }
}