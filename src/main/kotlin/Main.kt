
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import model.InverterType
import model.InverterData
import model.PowerPlantData
import org.kalasim.*
import org.kalasim.misc.AmbiguousDuration
import org.koin.core.context.startKoin
import simulation.Simulation
import simulation.SimulationData
import units.Inverter
import java.lang.Thread.sleep
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

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
            prosumerIdByPowerPlantId = mapOf()
        )

        startKoin {}

        val test = Simulation(simDa, 100, true)
        launch {
            test.run(30)
        }

        sleep(2_000)
        println("Hello")
        test.powerController.commandParks(mapOf(1 to 30))
        sleep(10_000)
        println("Bello")
        test.powerController.commandParks(mapOf(1 to 100))

        test.powerController.readParks()
    }
}