package park

import org.kalasim.TickTime

data class ParkPower(
    val powerPlantId: Int = 0,
    val power: Double = 0.0,
    val tickTime: TickTime = TickTime(0)
)