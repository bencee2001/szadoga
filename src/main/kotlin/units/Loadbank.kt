package units

import util.LogFlags
import constvalue.ConstValues
import event.LoadbankReadEvent
import org.kalasim.Component
import scheduler.TaskScheduler
import util.eventLogging
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


class Loadbank(
    loadbankId: Int,
    ratedAcPower: Double,
    private var temp: Double = 0.0,
    private var tempTarget: Double = 0.0,
    constants: ConstValues,
    startTargetOutput: Double = 0.0,
    hasError: Boolean
): AbstractUnit(
    id = loadbankId,
    type = UnitType.LOADBANK,
    ratedAcPower = ratedAcPower,
    constants = constants,
    taskScheduler = TaskScheduler(),
    targetOutput = startTargetOutput,
    hasError = hasError,
    lastReadPower = 0.0)  {

    private val defaultTemp : Double = 10.0
    private val startTemp :Double = 20.0
    private val maxTemp: Double = 90.0
    private val changeValue: Double = ratedAcPower.div(maxTemp.minus(startTemp))
    private val tempUpChangePerTick: Double = constants.UP_POWER_CONTROL_PER_TICK.div(changeValue)
    private val tempDownChangePerTick: Double = constants.DOWN_POWER_CONTROL_PER_TICK.div(changeValue)

    override fun repeatedProcess() = sequence<Component> {
        hold(1.minutes)
        changeTemperature()
        println("Loadbank $id: ${calculateConsumeFromTemp(temp)}, $temp, $tempTarget, $targetOutput, $now")
    }

    override fun read(loggingEnabled: Boolean): UnitPower {
        val power = super.read(loggingEnabled)
        eventLogging(LogFlags.UNIT_READ_LOG){
            log(
                LoadbankReadEvent(
                id = id,
                unitType = type,
                minConsume = 0,
                maxConsume = ratedAcPower.toInt(),
                consume = power.power.toInt(),
                unitPowerMessage = power.unitPowerMessage,
                temperature = temp.toInt(),
                currentTarget = targetOutput.toInt(),
                temperatureTarget = tempTarget.toInt(),
                now
                )
            )
        }
        return power
    }

    override fun readNewValue(): UnitPower {
        val consumedPower = if(temp < startTemp || targetOutput == 0.0){
            0.0
        }else{
            calculateConsumeFromTemp(temp)
        }
        lastReadTime = now
        lastReadPower = consumedPower
        return UnitPower(id, consumedPower, now, UnitPowerMessage.CONSUME)
    }

    override fun readOldValue(): UnitPower {
        return UnitPower(id, lastReadPower, lastReadTime, UnitPowerMessage.CONSUME)
    }

    override fun command(target: Double) {
        setTargets(target)
    }

    private fun setTargets(target: Double) {
        val calcTemp = calculateTempFromTarget(target)
        if(target != 0.0) {
            if (target > ratedAcPower) {
                targetOutput = ratedAcPower
                lastTargetCommand = ratedAcPower
                tempTarget = maxTemp
            } else {
                targetOutput = target
                lastTargetCommand = target
                tempTarget = calcTemp
            }
        }else{
            targetOutput = 0.0
            lastTargetCommand = 0.0
            tempTarget = 0.0
        }
    }

    fun canNextStart(): Boolean{
        return temp.div(maxTemp) > CAN_NEXT_START_PERCENTAGE
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

    private fun calculateConsumeFromTemp(temp: Double): Double{
        return changeValue.times(temp).minus(changeValue.times(startTemp))
    }

    private fun calculateTempFromTarget(power: Double): Double{
        return power.div(changeValue).plus(startTemp)
    }

    companion object{
        const val CAN_NEXT_START_PERCENTAGE = 0.75
    }
}