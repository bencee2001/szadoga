package event

import org.kalasim.Event
import org.kalasim.TickTime
import units.UnitPowerMessage
import units.UnitType

class InverterReadEvent(
    val unitId: Int,
    val unitType: UnitType,
    val minPower: Int,
    val maxPower: Int,
    val power: Int,
    val currentTarget: Int,
    val unitPowerMessage: UnitPowerMessage,
    time: TickTime
): Event(time)