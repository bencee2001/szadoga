package event

import org.kalasim.Event
import org.kalasim.TickTime

class ParkReadEvent(
    val powerPlantId: Int,
    val powerPlantName: String,
    val producedPower: Double,
    time: TickTime
): Event(time)