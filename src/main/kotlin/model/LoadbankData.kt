package model

import model.types.LoadbankType

data class LoadbankData(
    val loadbankId: Int,
    val powerPlantId: Int,
    val ratedAcPower: Int,
    val powerStepW: Int,
    val loadbankType: LoadbankType,
)