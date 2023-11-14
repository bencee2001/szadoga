
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import model.*
import model.types.BatteryType
import model.types.EngineType
import model.types.InverterType
import model.types.LoadbankType
import org.koin.core.context.startKoin
import simulation.Simulation
import simulation.SimulationData
import java.lang.Thread.sleep

suspend fun main(args: Array<String>){
    coroutineScope {
        val simDa = SimulationData(
            powerPlants = mapOf(
                1 to PowerPlantData(1, "Hello", 100.0),
                2 to PowerPlantData(2, "Bello", 200.0)
            ),
            inverters = mapOf(
                1 to InverterData(1, 1, 20.0, true, InverterType.HUAWEI),
                2 to InverterData(2, 1, 30.0, true, InverterType.TEST),
                3 to InverterData(3, 1, 10.0, true, InverterType.HUAWEI),
                4 to InverterData(4, 1, 25.0, true, InverterType.HUAWEI)
            ),
            loadbanks = mapOf(
                1 to LoadbankData(1, 1, 20, 2, LoadbankType.TEST)
            ),
            powerPlantIdByProsumerId = mapOf(
                1 to 1,
                2 to 2
            ),
            engines = mapOf(
                1 to EngineData(1, 2, 100.0, 40.0, EngineType.TEST),
                2 to EngineData(2, 2, 100.0, 45.0, EngineType.TEST)
            ),
            batteries = mapOf(
                1 to BatteryData(1,1,100.0,BatteryType.TEST)
            )
        )

        startKoin {}

        var i = 0

        val test = Simulation(simDa, 100, true)
        launch {
            test.run(60)
        }

        while(true){
            i += 2
            println(test.powerController.readParks())
            if(i < 30) {
                test.powerController.commandParks(mapOf(1 to 50, 2 to 70))
            }else{
                test.powerController.commandParks(mapOf(1 to 20, 2 to 25))
            }
            sleep(2_000)
        }
    }
}