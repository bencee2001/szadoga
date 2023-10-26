
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


class TestSim(private val bool: Boolean): Environment(durationUnit = DurationUnit.SECONDS){
    lateinit var inverter: Inverter

    init {
        this.apply {
            //inverter = Inverter(1,10F,20F,30F,1F,0,0,TickTime(0),true)
            if(bool) ClockSync(tickDuration = 1.seconds)
        }
    }
}

fun Environment.createSimulation(builder: Environment.() -> Unit) {
    this.apply(builder)
}

@OptIn(AmbiguousDuration::class)
suspend fun main(args: Array<String>)= coroutineScope{

    val simDa = SimulationData(
        powerPlants = mapOf(
            1 to PowerPlantData(1, "Hello", 100F)
        ),
        inverters = mapOf(
            1 to InverterData(1,1,25F, true, InverterType.HUAWEI),
            2 to InverterData(2,1,25F, true, InverterType.HUAWEI),
            3 to InverterData(3,1,25F, true, InverterType.HUAWEI),
            4 to InverterData(4,1,25F, true, InverterType.HUAWEI)
        ),
        prosumerIdByPowerPlantId = mapOf()
    )

    startKoin{}

    val test = Simulation(simDa, true)
    launch {
        test.run(30)
    }

    sleep(3_000)

    println(test.powerController.readParks())

    sleep(7_000)

    println(test.powerController.readParks())

    sleep(5_000)

    println(test.powerController.readParks())

    sleep(6_000)

    println(test.powerController.readParks())



    /*val test = TestSim(false)

    launch {
        test.run(30)
    }
    createSimulation {  }

    for(i in 1..30){
        sleep(1000)
        println("OutSide: ${test.inverter.read()}")
    }*/




    /*val inverterEventList = mutableListOf<InverterEvent>()
    val eventList = mutableListOf<PowerControlEvent>()
    /*val simDa = SimulationData(
        powerPlants = mapOf(
            1 to SimPowerPlant(1, "Hello", 100F)
        ),
        inverters = mapOf(
            1 to SimInverter(1,1,25F, true, InverterType.HUAWEI),
            2 to SimInverter(2,1,25F, true, InverterType.HUAWEI),
            3 to SimInverter(3,1,25F, true, InverterType.HUAWEI),
            4 to SimInverter(4,1,25F, true, InverterType.HUAWEI)
        ),
        prosumerIdByPowerPlantId = mapOf()
    )
    SimulationBuilder(simDa).start(30)*/



    val env = createSimulation(durationUnit = DurationUnit.SECONDS) {

        ClockSync(tickDuration = 1.seconds)
        addEventListener{ it: InverterEvent -> inverterEventList.add(it) }
        addEventListener{ it: PowerControlEvent -> eventList.add(it) }


        val inverterList = mutableListOf<Inverter>()
        inverterList.add(
            Inverter(1,
                2.2F,
                10F,
                12F,
                1F,
                0,
                0,
                now,
                true)
        )
        inverterList.add(
            Inverter(2,
                5F,
                10F,
                12F,
                1F,
                0,
                0,
                now,
                true)
        )
        inverterList.add(
            Inverter(3,
                7F,
                10F,
                12F,
                1F,
                0,
                0,
                now,
                true)
        )
        inverterList.add(
            Inverter(4,
                10F,
                10F,
                12F,
                1F,
                0,
                0,
                now,
                true)
        )

        val invRouter = InverterRouter(1, inverterList)
        val park = Park(1,"", inverterList)


        /*val invPark = Park(1,"Test",inverterList)
        invPark.setTargetPower(28F)*/
        val pC = PowerController(listOf(park))
        val test = Test(pC)

    /*addEventListener{it: CntEvent -> eventList.add(it)}


        val list = mutableListOf<Counter>()
        list.add(Counter())
        list.add(Counter())
        list.add(Counter())
        ReaderCommander(list)*/

    }
    env.run(30)

    env.

    inverterEventList.toDataFrame().writeCSV(File("C:\\Users\\Bence\\Documents\\InvTest.csv"))
    eventList.toDataFrame().writeCSV(File("C:\\Users\\Bence\\Documents\\ParkTest.csv"))
    println(eventList.toDataFrame())

    fun test(park: Park){
        park.setTargetPower(20F)
    }*/

}