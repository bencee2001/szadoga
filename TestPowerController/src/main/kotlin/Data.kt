import model.InverterData
import model.InverterType
import model.PowerPlantData
import simulation.SimulationData

class Data {
    companion object{
        val simData = SimulationData(
            powerPlants = mapOf(
                1 to PowerPlantData(1, "Hello", 100.0)
            ),
            inverters = mapOf(
                1 to InverterData(1, 1, 20.0, true, InverterType.HUAWEI),
                2 to InverterData(2, 1, 30.0, true, InverterType.TEST),
                3 to InverterData(3, 1, 10.0, true, InverterType.HUAWEI),
                4 to InverterData(4, 1, 25.0, true, InverterType.HUAWEI)
            ),
            prosumerIdByPowerPlantId = mapOf()
        )
    }
}