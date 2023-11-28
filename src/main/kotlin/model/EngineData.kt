package model

import model.types.EngineType

data class EngineData(
    val engineId: Int,
    val powerPlantId: Int,
    val ratedAcPower: Double,
    val minimumRunningPower: Double,
    val heatUpTime: Int,
    val engineType: EngineType,
)