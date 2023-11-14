package model

import model.types.BatteryType

data class BatteryData (
    val batteryId: Int,
    val powerPlantId: Int,
    val ratedAcPower: Double,
    val batteryType: BatteryType
)