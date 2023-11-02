package simulation

import constvalue.inverter.InverterTypeConst
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import model.InverterType
import model.InverterData
import org.kalasim.ClockSync
import org.kalasim.Environment
import org.kalasim.TickTime
import park.Park
import powercontrol.PowerController
import units.Inverter
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class Simulation(simData: SimulationData, randomSeed: Int, inRealTime: Boolean): Environment(randomSeed = randomSeed) {

    lateinit var powerController: PowerController

    init {
        this.apply {
            if(inRealTime) ClockSync(tickDuration = 1.seconds)
            powerController = setPowerController(simData)
            //powerController.commandParks(mapOf(1 to 30)) //TODO
        }
    }

    private fun setPowerController(simData: SimulationData): PowerController{
        val parkList = mutableListOf<Park>()
        val inverterIdsByPowerPlantId = simData.inverters.map { it.value }.groupBy { it.powerPlantId }
            .mapValues { inv -> inv.value.map { it.inverterId } }
        inverterIdsByPowerPlantId.keys.forEach { powerPlantId ->
            val inverters = simData.inverters.values.filter { inv -> inv.inverterId in inverterIdsByPowerPlantId[powerPlantId]!!}
            val inverterUnit = toInverterUnit(inverters)
            parkList.add(
                Park(
                    parkId = simData.powerPlants[powerPlantId]!!.powerPlantId,
                    parkName = simData.powerPlants[powerPlantId]!!.powerPlantName,
                    unitList = inverterUnit.associateBy { it.id },
                    maximumOutput = simData.powerPlants[powerPlantId]!!.maxPowerOutPut
                )
            )
        }
        return PowerController(parkList)
    }

    private fun toInverterUnit(inverters: List<InverterData>): List<Inverter> {
        val inverterUnit = inverters.map { inv ->
            val invDefVal = getInverterConst(inv)
            Inverter(
                inverterId = inv.inverterId,
                targetProsume = 0.0,
                prosume = 0.0,
                maxAllowedAcPower = inv.maxAllowedAcPower,
                constValues = invDefVal,
                lastReadTime = TickTime(0),
                isReadable = true
            )
        }
        return inverterUnit
    }

    private fun getInverterConst(inv: InverterData) = InverterTypeConst.map[inv.type] ?: error("")

}