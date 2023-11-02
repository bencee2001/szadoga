package event

import kotlinx.datetime.Instant
import org.kalasim.Event
import org.kalasim.TickTime

class ParkEvent(
    val powerPlantId: Int,
    val powerPlantName: String,
    //val parkType: String,   TODO kell???
    val producedPower: Double,
    time: TickTime
): Event(time)