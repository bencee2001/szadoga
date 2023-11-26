package event

import org.kalasim.Event
import org.kalasim.TickTime

class InverterEvent(
    val inverterId: Int,
    val producing: Double,
    time: TickTime
): Event(time){
    val tick: Int

    init {
        tick = time.toString().toInt()
    }

}