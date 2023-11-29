package event

import org.kalasim.Event
import org.kalasim.TickTime

class ParkReadEvent(
    val powerPlantId: Int,
    val powerPlantName: String,
    val producedPower: Double,
    val targetPower: Int,
    time: TickTime
): Event(time)