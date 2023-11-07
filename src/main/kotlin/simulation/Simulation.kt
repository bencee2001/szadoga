package simulation

import constvalue.ConstByType
import constvalue.loadbank.TestLoadbank
import model.InverterData
import model.LoadbankData
import org.kalasim.ClockSync
import org.kalasim.Environment
import park.Park
import powercontrol.PowerController
import units.AbstractUnit
import units.Inverter
import units.Loadbank
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
        val inverterIdsByPowerPlantId = getInverterIdsByPowerPlantId(simData)
        val loadbankIdsByPowerPlantId = getLoadbankIdsByPowerPlantId(simData)

        inverterIdsByPowerPlantId.keys.forEach { powerPlantId ->
            val inverters = simData.inverters.values.filter { inv -> inv.inverterId in inverterIdsByPowerPlantId[powerPlantId]!!}
            val loadbanks = simData.loadbanks.values.filter { ld -> ld.loadbankId in loadbankIdsByPowerPlantId[powerPlantId]!!}
            val inverterUnits = toInverterUnits(inverters)
            val loadbankUnits = toLoadbankUnits(loadbanks)
            val units = mutableListOf<AbstractUnit>()
            units.addAll(inverterUnits)
            units.addAll(loadbankUnits)
            parkList.add(
                Park(
                    parkId = simData.powerPlants[powerPlantId]!!.powerPlantId,
                    parkName = simData.powerPlants[powerPlantId]!!.powerPlantName,
                    unitList = units,
                    maximumOutput = simData.powerPlants[powerPlantId]!!.maxPowerOutPut
                )
            )
        }
        return PowerController(parkList)
    }

    private fun toLoadbankUnits(loadbanks: List<LoadbankData>): List<Loadbank> {
        val loadbankUnits = loadbanks.map { ld ->
            val defVal = TestLoadbank
            Loadbank(
                loadbankId =  ld.loadbankId,
                temp = 0.0,
                tempTarget = 0.0,
                constants = defVal,
                targetOutput = 0.0,
                hasError = false
            )
        }
        return loadbankUnits
    }

    private fun getLoadbankIdsByPowerPlantId(simData: SimulationData): Map<Int, List<Int>> {
        val loadbankIdByProsumerId = simData.loadbanks.values.groupBy { it.prosumerId }
            .mapValues { ld -> ld.value.map { it.loadbankId } }
        return loadbankIdByProsumerId.mapKeys { simData.powerPlantIdByProsumerId[it.key]!! }
    }

    private fun getInverterIdsByPowerPlantId(simData: SimulationData) = simData.inverters.map { it.value }.groupBy { it.powerPlantId }
        .mapValues { inv -> inv.value.map { it.inverterId } }

    private fun toInverterUnits(inverters: List<InverterData>): List<Inverter> {
        return inverters.map { inv ->
            val invDefVal = getInverterConst(inv)
            Inverter(
                inverterId = inv.inverterId,
                target = 0.0,
                prosume = 0.0,
                constants = invDefVal,
                hasError = false
            )
        }
    }

    private fun getInverterConst(inv: InverterData) = ConstByType.map[inv.type] ?: error("")

}