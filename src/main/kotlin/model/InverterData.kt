package model

data class InverterData (
    val inverterId: Int,
    val powerPlantId: Int,
    val maxAllowedAcPower: Double,
    val controllable: Boolean,
    val type: InverterType
)
