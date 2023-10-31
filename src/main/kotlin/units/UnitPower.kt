package units

import org.kalasim.TickTime

data class UnitPower(
    val unitId: Int,
    val power: Double,
    val tickTime: TickTime,
    val unitPowerMessage: UnitPowerMessage
)