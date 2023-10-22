package event

import org.kalasim.Event
import org.kalasim.TickTime

class PowerControlEvent(
    val parkId: Int,
    val produced: Float,
    time: TickTime
): Event(time)