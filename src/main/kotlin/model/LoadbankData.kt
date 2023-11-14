package model

import model.types.LoadbankType

data class LoadbankData(
    val loadbankId: Int,
    val type: LoadbankType,
    val ratedAcPower: Int,
    val powerStepW: Int,  //TODO  Constants-ban UP/DOWN PowerController
    val prosumerId: Int
)