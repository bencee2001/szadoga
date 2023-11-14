package simulation

import constvalue.ConstByType
import constvalue.engine.TestEngine
import constvalue.loadbank.TestLoadbank
import model.BatteryData
import model.EngineData
import model.InverterData
import model.LoadbankData
import org.kalasim.ClockSync
import org.kalasim.Environment
import park.Park
import powercontrol.PowerController
import units.*
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
        val powerPlantIds = simData.powerPlants.keys
        val inverterIdsByPowerPlantId = getInverterIdsByPowerPlantId(simData)
        val loadbankIdsByPowerPlantId = getLoadbankIdsByPowerPlantId(simData)
        val engineIdsByPowerPlantId = getEngineIdsByPowerPlant(simData)
        val batteryIdsByPowerPlantId = getBatteryIdsByPowerPlant(simData)

        powerPlantIds.forEach { powerPlantId ->
            val inverters = simData.inverters.values.filter { inv ->
                inv.inverterId in (inverterIdsByPowerPlantId[powerPlantId] ?: emptyList())
            }
            val loadbanks = simData.loadbanks.values.filter { ld ->
                ld.loadbankId in (loadbankIdsByPowerPlantId[powerPlantId] ?: emptyList())
            }
            val engines = simData.engines.values.filter { eng ->
                eng.engineId in (engineIdsByPowerPlantId[powerPlantId] ?: emptyList())
            }
            val batteries = simData.batteries.values.filter { bty ->
                bty.batteryId in (batteryIdsByPowerPlantId[powerPlantId] ?: emptyList())
            }
            val inverterUnits = toInverterUnits(inverters)
            val loadbankUnits = toLoadbankUnits(loadbanks)
            val engineUnits = toEngineUnits(engines)
            val batteryUnits: List<Battery> = toBatteryUnits(batteries)
            val units = mutableListOf<AbstractUnit>()
            units.addAll(inverterUnits)
            units.addAll(loadbankUnits)
            units.addAll(engineUnits)
            units.addAll(batteryUnits)
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

    private fun getLoadbankIdsByPowerPlantId(simData: SimulationData): Map<Int, List<Int>> {
        val loadbankIdByProsumerId = simData.loadbanks.values.groupBy { it.prosumerId }
            .mapValues { ld -> ld.value.map { it.loadbankId } }
        return loadbankIdByProsumerId.mapKeys { simData.powerPlantIdByProsumerId[it.key]!! }
    }

    private fun getInverterIdsByPowerPlantId(simData: SimulationData) = simData.inverters.map { it.value }.groupBy { it.powerPlantId }
        .mapValues { inv -> inv.value.map { it.inverterId } }

    private fun getEngineIdsByPowerPlant(simData: SimulationData) =
        simData.engines.map { it.value }.groupBy { it.powerPlantId }.mapValues { eng -> eng.value.map { it.engineId } }

    private fun getBatteryIdsByPowerPlant(simData: SimulationData) =
        simData.batteries.values.groupBy { it.batteryId }.mapValues { bty -> bty.value.map { it.batteryId } }

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


    private fun toLoadbankUnits(loadbanks: List<LoadbankData>): List<Loadbank> {
        val loadbankUnits = loadbanks.map { ld ->
            val defVal = TestLoadbank
            Loadbank(
                loadbankId =  ld.loadbankId,
                temp = 0.0,
                tempTarget = 0.0,
                constants = defVal,
                startTargetOutput = 0.0,
                hasError = false
            )
        }
        return loadbankUnits
    }

    private fun toEngineUnits(engines: List<EngineData>): List<Engine> {
        val engineUnits = engines.map{ eng ->
            val defVal = TestEngine
            Engine(
                engineId = eng.engineId,
                minimumRunningPower = eng.minimumRunningPower,
                constants = defVal,
                targetOutput = 0.0,
                produce = 0.0,
                heatUpTimeInTick = 5,
                hasError = false,
                isStarted = false,
            )
        }
        return engineUnits
    }

    private fun toBatteryUnits(batteries: List<BatteryData>): List<Battery> {
        val batteryUnits = batteries.map { bty ->
            val defVal = TestEngine
            Battery(
                batteryId = bty.batteryId,
                target = 0.0,
                constants = defVal,
                charge = 90.0,
                hasError = false
            )
        }
        return batteryUnits
    }

    private fun getInverterConst(inv: InverterData) = ConstByType.map[inv.type] ?: error("")
}
