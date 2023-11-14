package model

import model.types.InverterType

data class InverterData (
    val inverterId: Int,
    val powerPlantId: Int,
    val ratedAcPower: Double,
    val controllable: Boolean,
    val type: InverterType
)
