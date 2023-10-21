
import component.*
import event.InverterEvent
import krangl.asDataFrame
import krangl.writeCSV
import org.kalasim.*
import java.io.File
import kotlin.time.DurationUnit

fun main(args: Array<String>) {
    val eventList = mutableListOf<InverterEvent>()
    val env = createSimulation(durationUnit = DurationUnit.SECONDS) {

        addEventListener{it: InverterEvent -> eventList.add(it)}

        val inverterList = mutableListOf<Inverter>()
        inverterList.add(Inverter(1,2.2F,10F,12F,1F,4, now, 2F))
        val invPark = Park(1,"Test",inverterList)

        /*addEventListener{it: CntEvent -> eventList.add(it)}
        //ClockSync(tickDuration = 1.seconds)

        val list = mutableListOf<Counter>()
        list.add(Counter())
        list.add(Counter())
        list.add(Counter())
        ReaderCommander(list)*/

    }

    env.run(30)
    eventList.asDataFrame().writeCSV(File("C:\\Users\\Bence\\Documents\\invEventTest.csv"))
    println(eventList.asDataFrame())


}