
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import model.*
import org.koin.core.context.startKoin
import simulation.Simulation
import simulation.SimulationData
import java.lang.Thread.sleep

suspend fun main(args: Array<String>){
    coroutineScope {
        val simDa = SimulationData(
            powerPlants = mapOf(
                1 to PowerPlantData(1, "Hello", 100.0)
            ),
            inverters = mapOf(
                1 to InverterData(1, 1, 20.0, true, InverterType.HUAWEI),
                2 to InverterData(2, 1, 30.0, true, InverterType.TEST),
                3 to InverterData(3, 1, 10.0, true, InverterType.HUAWEI),
                4 to InverterData(4, 1, 25.0, true, InverterType.HUAWEI)
            ),
            loadbanks = mapOf(
                1 to LoadbankData(1, LoadbankType.UNKNOWN, 20, 2, 1)
            ),
            powerPlantIdByProsumerId = mapOf(
                1 to 1
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
                test.powerController.commandParks(mapOf(1 to 50))
            }else{
                test.powerController.commandParks(mapOf(1 to 20))
            }
            sleep(2_000)
        }
    }
}