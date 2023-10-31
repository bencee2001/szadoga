package simulation

import model.InverterData
import model.PowerPlantData


//PostgreData convert-nél kivenni refernciákat
//powerplantId-hoz egységeket
data class SimulationData (
    val powerPlants: Map<Int, PowerPlantData> = emptyMap(),
    val inverters: Map<Int, InverterData> = emptyMap(),
    val prosumerIdByPowerPlantId: Map<Int,Int>,
)