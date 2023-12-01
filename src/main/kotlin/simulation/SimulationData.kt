package simulation

import model.*

data class SimulationData (
    val powerPlants: Map<Int, PowerPlantData> = emptyMap(),
    val inverters: Map<Int, InverterData> = emptyMap(),
    val loadbanks: Map<Int, LoadbankData> = emptyMap(),
    val engines: Map<Int, EngineData> = emptyMap(),
    val batteries: Map<Int, BatteryData> = emptyMap()
)