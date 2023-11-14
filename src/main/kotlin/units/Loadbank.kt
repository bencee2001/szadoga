package units

import constvalue.ConstValues
import org.kalasim.Component
import scheduler.TaskScheduler


class Loadbank(
    loadbankId: Int,
    private var temp: Double = 0.0,
    private var tempTarget: Double = 0.0,
    constants: ConstValues,
    startTargetOutput: Double = 0.0,
    hasError: Boolean
): AbstractUnit(loadbankId, type = UnitType.LOADBANK, constants, TaskScheduler(), startTargetOutput, hasError)  {

    private val defaultTemp : Double = 10.0
    private val startTemp :Double = 20.0
    private val maxTemp: Double = 90.0
    private val changeValue: Double = constants.RATED_AC_POWER.div(maxTemp.minus(startTemp))
    private val tempUpChangePerTick: Double = constants.UP_POWER_CONTROL_PER_TICK.div(changeValue)
    private val tempDownChangePerTick: Double = constants.DOWN_POWER_CONTROL_PER_TICK.div(changeValue)

    override fun repeatedProcess() = sequence<Component> {
        hold(1)
        changeTemperature()
        println("Loadbank $id: ${calculateConsumeFromTemp(temp)}, $temp, $tempTarget, $targetOutput, $now")
    }

    override fun read(): UnitPower {
        val consumedPower = if(temp < startTemp ||targetOutput == 0.0){
            0.0
        }else{
            calculateConsumeFromTemp(temp)
        }
         return UnitPower(id, consumedPower, now, UnitPowerMessage.CONSUME)
    }

    override fun command(target: Double) {
        println("Command: $target")
        setTargets(target)
    }

    private fun setTargets(target: Double) {
        val calcTemp = calculateTempFromTarget(target)
        if(target != 0.0) {
            if (target > constants.RATED_AC_POWER) {
                targetOutput = constants.RATED_AC_POWER
                tempTarget = maxTemp
            } else {
                targetOutput = target
                tempTarget = calcTemp
            }
        }else{
            targetOutput = 0.0
            tempTarget = 0.0
        }
    }

    fun canNextStart(): Boolean{
        return temp.div(maxTemp) > 0.75
    }

    private fun changeTemperature() {
        if(tempTarget == 0.0){
            changeTempToLimit(defaultTemp)
        }else{
            changeTempToLimit(tempTarget)
        }
    }

    private fun changeTempToLimit(borderValue: Double) {
        if (temp != borderValue) {
            if (temp < borderValue) {
                increaseTemp(borderValue)
            } else {
                decreaseTemp(borderValue)
            }
        }
    }

    private fun increaseTemp(borderValue: Double){
        val newTemp = temp + tempUpChangePerTick
        temp = if(newTemp > borderValue)
            borderValue
        else
            newTemp
    }

    private fun decreaseTemp(borderValue: Double){
        val newTemp = temp - tempDownChangePerTick
        temp = if(newTemp < borderValue)
            borderValue
        else
            newTemp
    }

    fun calculateConsumeFromTemp(temp: Double): Double{
        return changeValue.times(temp).minus(changeValue.times(startTemp))
    }

    fun calculateTempFromTarget(power: Double): Double{
        return power.div(changeValue).plus(startTemp)
    }
}