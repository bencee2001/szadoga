package simulation

import model.*


//PostgreData convert-nél kivenni refernciákat
//powerplantId-hoz egységeket
data class SimulationData (
    val powerPlants: Map<Int, PowerPlantData> = emptyMap(),
    val inverters: Map<Int, InverterData> = emptyMap(),
    val loadbanks: Map<Int, LoadbankData> = emptyMap(),
    val engines: Map<Int, EngineData> = emptyMap(),
    val powerPlantIdByProsumerId: Map<Int,Int> = emptyMap(),
    val batteries: Map<Int, BatteryData> = emptyMap()
)