package event

import org.kalasim.Event
import org.kalasim.TickTime
import units.UnitPowerMessage
import units.UnitType

class BatteryReadEvent(
    val id: Int,
    val unitType: UnitType = UnitType.BATTERY,
    val minPower: Int,
    val maxPower: Int,
    val minCharge: Int,
    val maxCharge: Int,
    val charge: Int,
    val prosume: Int,
    val unitPowerMessage: UnitPowerMessage,
    val currentTarget: Int,
    time: TickTime
): Event(time)