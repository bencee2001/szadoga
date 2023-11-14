package model

import model.types.LoadbankType

data class LoadbankData(
    val loadbankId: Int,
    val prosumerId: Int,
    val ratedAcPower: Int,
    val powerStepW: Int,  //TODO  Constants-ban UP/DOWN PowerController
    val loadbankType: LoadbankType,
)