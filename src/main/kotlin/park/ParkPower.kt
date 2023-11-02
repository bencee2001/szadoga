package park

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.kalasim.TickTime

data class ParkPower(
    val powerPlantId: Int = 0,
    val power: Double = 0.0,
    val tickTime: TickTime = TickTime(0)
)