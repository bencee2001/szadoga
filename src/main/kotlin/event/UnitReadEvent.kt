package event

import kotlinx.datetime.Instant
import org.kalasim.Event
import org.kalasim.TickTime

class UnitReadEvent(
    val unitId: Int,
    val power: Double,
    time: TickTime
): Event(time)