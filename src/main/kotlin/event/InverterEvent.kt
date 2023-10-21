package event

import org.kalasim.Event
import org.kalasim.TickTime

class InverterEvent(
    val inverterId: Int,
    val producing: Float,
    time: TickTime
): Event(time)