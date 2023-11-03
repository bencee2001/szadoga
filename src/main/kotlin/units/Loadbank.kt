package units

import constvalue.inverter.ConstValues
import org.kalasim.Component
import scheduler.TaskScheduler

/**
 * TODO
 *
 * @property consume
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
    private var temp: Double = 0.0,
    private var tempTarget: Double = 0.0,
    constants: ConstValues,
    targetOutput: Double = 0.0,
    hasError: Boolean
): AbstractUnit(loadbankId, type = UnitType.LOADBANK, constants, TaskScheduler(), targetOutput, hasError)  {

    private val maxTemp: Double = 100.0

    override fun repeatedProcess() = sequence<Component> {
        changeTemperature()
    }

    override fun read(): UnitPower {
         return UnitPower(id, calculateConsumeFromTemp(temp), now, UnitPowerMessage.CONSUME)
    }

    override fun command(target: Double) {
        tempTarget = calculateTempFromTarget(target)

    }

    fun canNextStart(): Boolean{
        return temp.div(maxTemp) > 0.75
    }

    private fun changeTemperature() {
        if(tempTarget != 0.0){
            val newTemp = temp + constants.UP_POWER_CONTROL_PER_TICK
            temp = if(newTemp > maxTemp){
                maxTemp
            } else {
                newTemp
            }
        } else {
            val newTemp = temp - constants.DOWN_POWER_CONTROL_PER_TICK
            temp = if(newTemp < 0.0){
                maxTemp
            } else {
                newTemp
            }
        }
    }

    private fun calculateConsumeFromTemp(temp: Double): Double{
        val powerPerTemp = constants.RATED_AC_POWER.div(maxTemp)
        return powerPerTemp.times(temp)
    }

    private fun calculateTempFromTarget(power: Double): Double{
        val powerPerTemp = constants.RATED_AC_POWER.div(maxTemp)
        return power.div(powerPerTemp)
    }
}