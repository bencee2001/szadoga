package event

import org.kalasim.Event
import org.kalasim.TickTime
import units.UnitType

class LoadbankReadEvent(
    val id: Int,
    val unitType: UnitType = UnitType.LOADBANK,
    val minConsume: Int,
    val maxConsume: Int,
    val consume: Int,
    val temperature: Int,
    val currentTarget: Int,
    val temperatureTarget: Int,
    time: TickTime
): Event(time){

}