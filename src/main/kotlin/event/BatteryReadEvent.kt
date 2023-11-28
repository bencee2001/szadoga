package event

import org.kalasim.Event
import org.kalasim.TickTime
import units.UnitType

class BatteryReadEvent(
    val id: Int,
    val unitType: UnitType = UnitType.BATTERY,
    val minPower: Int,
    val maxPower: Int,
    val power: Int,
    val currentTarget: Int,
    time: TickTime
): Event(time)