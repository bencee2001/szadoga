package event

import org.kalasim.Event
import org.kalasim.TickTime
import units.UnitPowerMessage
import units.UnitType

class EngineReadEvent(
    val unitId: Int,
    val unitType: UnitType,
    val minPower: Int,
    val maxPower: Int,
    val power: Int,
    val unitPowerMessage: UnitPowerMessage,
    val currentTarget: Int,
    time: TickTime
): Event(time)