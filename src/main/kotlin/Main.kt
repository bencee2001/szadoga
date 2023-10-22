
import component.*
import event.InverterEvent
import event.ParkEvent
import event.PowerControlEvent
import krangl.asDataFrame
import krangl.writeCSV
import org.kalasim.*
import park.Park
import powercontrol.PowerController
import java.io.File
import kotlin.time.DurationUnit

fun main(args: Array<String>) {
    val inverterEventList = mutableListOf<InverterEvent>()
    val eventList = mutableListOf<PowerControlEvent>()
    val env = createSimulation(durationUnit = DurationUnit.SECONDS) {

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
                true))
        inverterList.add(
            Inverter(2,
                5F,
                10F,
                12F,
                1F,
                0,
                0,
                now,
                true))
        inverterList.add(
            Inverter(3,
                7F,
                10F,
                12F,
                1F,
                0,
                0,
                now,
                true))
        inverterList.add(
            Inverter(4,
                10F,
                10F,
                12F,
                1F,
                0,
                0,
                now,
                true))

        val invPark = Park(1,"Test",inverterList)
        invPark.setTargetPower(28F)
        val pC = PowerController(listOf(invPark))

        /*addEventListener{it: CntEvent -> eventList.add(it)}
        //ClockSync(tickDuration = 1.seconds)

        val list = mutableListOf<Counter>()
        list.add(Counter())
        list.add(Counter())
        list.add(Counter())
        ReaderCommander(list)*/

    }

    env.run(30)
    inverterEventList.asDataFrame().writeCSV(File("C:\\Users\\Bence\\Documents\\InvTest.csv"))
    eventList.asDataFrame().writeCSV(File("C:\\Users\\Bence\\Documents\\ParkTest.csv"))
    println(eventList.asDataFrame())


}