import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import simulation.Simulation
import java.lang.Thread.sleep

suspend fun main() {
    coroutineScope {

        val simulation = Simulation(Data.simData, 42, true)
        launch {
            simulation.run(120)
        }
        var i = 0
        var command = 30

        while(true){
            i += 2
            val powers = simulation.powerController.readParks()
            println(powers)
            if(i == 60)
                command = 60

            simulation.powerController.commandParks(mapOf(1 to command))
            sleep(2_000)
        }

    }
}