package simulation

import LogFlags
import configdsl.ConfigDSL
import configdsl.models.DslTask
import configdsl.models.DslUnit
import constvalue.ConstByType
import constvalue.ConstValues
import constvalue.CustomValues
import event.ParkReadEvent
import event.UnitReadEvent
import model.BatteryData
import model.EngineData
import model.InverterData
import model.LoadbankData
import model.types.UnitSubType
import org.jetbrains.kotlinx.dataframe.api.drop
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.writeCSV
import org.joda.time.DateTime
import org.kalasim.ClockSync
import org.kalasim.Environment
import park.Park
import powercontrol.PowerController
import units.*
import util.PATH
import util.fileNameDateFormater
import kotlin.time.Duration.Companion.seconds

class Simulation(simData: SimulationData, randomSeed: Int, inRealTime: Boolean, private val config: ConfigDSL? = null): Environment(randomSeed = randomSeed) {

    lateinit var powerController: PowerController

    val unitEventLog = mutableListOf<UnitReadEvent>()
    val parkEventLog = mutableListOf<ParkReadEvent>()

    init {
        this.apply {
            if(inRealTime) ClockSync(tickDuration = 1.seconds)
            if(LogFlags.UNIT_READ_LOG) addEventListener{ it: UnitReadEvent -> unitEventLog.add(it) }
            if(LogFlags.PARK_READ_LOG) addEventListener{ it: ParkReadEvent -> parkEventLog.add(it) }
            powerController = setPowerController(simData)
        }
    }

    fun runWithSave(time: Int, unitLogFileName: String? = null, parkLogFileName: String? = null) {
        val startDate = DateTime()
        run(time)
        if (LogFlags.UNIT_READ_LOG) {
            val name = unitLogFileName?.let { "$it.csv" } ?: "unitLog${fileNameDateFormater.print(startDate)}.csv"
            unitEventLog.toDataFrame().drop(2)
                .writeCSV("${PATH}\\$name")

        }
        if (LogFlags.PARK_READ_LOG) {
            val name = parkLogFileName?.let { "$it.csv" } ?: "parkLog${fileNameDateFormater.print(startDate)}.csv"
            parkEventLog.toDataFrame()
                .writeCSV("${PATH}\\$name")
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

    private fun getInverterIdsByPowerPlantId(simData: SimulationData) = simData.inverters.map { it.value }.groupBy { it.powerPlantId }
        .mapValues { inv -> inv.value.map { it.inverterId } }

    private fun getLoadbankIdsByPowerPlantId(simData: SimulationData) = simData.loadbanks.map { it.value }.groupBy { it.powerPlantId }
        .mapValues { ld -> ld.value.map { it.loadbankId }  }


    private fun getEngineIdsByPowerPlant(simData: SimulationData) =
        simData.engines.map { it.value }.groupBy { it.powerPlantId }.mapValues { eng -> eng.value.map { it.engineId } }

    private fun getBatteryIdsByPowerPlant(simData: SimulationData) =
        simData.batteries.values.groupBy { it.batteryId }.mapValues { bty -> bty.value.map { it.batteryId } }

    private fun toInverterUnits(inverters: List<InverterData>): List<Inverter> {
        return inverters.map { inv ->
            val constants = getConstValues(UnitType.INVERTER, inv.inverterType, inv.ratedAcPower)
            val (unitDefValues, unitTasks) = getDefValues(UnitType.INVERTER, inv.inverterId)
            val inverter = toInverterUnit(unitDefValues, inv, constants)
            addTaskToUnit(unitTasks, inverter)
            inverter
        }
    }

    private fun toLoadbankUnits(loadbanks: List<LoadbankData>): List<Loadbank> {
        return loadbanks.map { ld ->
            val defVal = getConstValues(UnitType.LOADBANK, ld.loadbankType)
            val (unitDefValues, unitTasks) = getDefValues(UnitType.INVERTER, ld.loadbankId)
            val loadbank = toLoadbankUnit(unitDefValues, ld, defVal)
            addTaskToUnit(unitTasks, loadbank)
            loadbank
        }
    }

    private fun toEngineUnits(engines: List<EngineData>): List<Engine> {
        return engines.map{ eng ->
            val defVal = getConstValues( UnitType.ENGINE ,eng.engineType)
            val (unitDefValues, unitTasks) = getDefValues(UnitType.ENGINE, eng.engineId)
            val engine = toEngineUnit(eng, defVal, unitDefValues)
            addTaskToUnit(unitTasks, engine)
            engine
        }
    }

    private fun toBatteryUnits(batteries: List<BatteryData>): List<Battery> {
        return batteries.map { bty ->
            val defVal = getConstValues( UnitType.BATTERY ,bty.batteryType)
            val (unitDefValues, unitTasks) = getDefValues(UnitType.BATTERY, bty.batteryId)
            val battery = toBatteryUnit(bty, unitDefValues, defVal)
            addTaskToUnit(unitTasks, battery)
            battery
        }

    }

    private fun toInverterUnit(
        unitDefValues: DslUnit?,
        inv: InverterData,
        constants: ConstValues
    ) = Inverter(
            inverterId = inv.inverterId,
            ratedAcPower = inv.ratedAcPower,
            target = unitDefValues?.targetOutput ?: getDefaultProducing(UnitType.INVERTER, inv.ratedAcPower) ?: 0.0,
            produce = 0.0,
            constants = constants,
            hasError = unitDefValues?.hasError ?: false
        )

    private fun toLoadbankUnit(
        unitDefValues: DslUnit?,
        ld: LoadbankData,
        defVal: ConstValues
    ) = Loadbank(
            loadbankId = ld.loadbankId,
            temp = 0.0,
            tempTarget = 0.0,
            ratedAcPower = ld.ratedAcPower.toDouble(),
            constants = defVal,
            startTargetOutput = unitDefValues?.targetOutput ?: getDefaultProducing(UnitType.LOADBANK, ld.ratedAcPower.toDouble()) ?: 0.0,
            hasError = unitDefValues?.hasError ?: false
        )

    private fun toEngineUnit(
        eng: EngineData,
        defVal: ConstValues,
        unitDefValues: DslUnit?
    ) = Engine(
        engineId = eng.engineId,
        minimumRunningPower = eng.minimumRunningPower,
        ratedAcPower = eng.ratedAcPower,
        constants = defVal,
        targetOutput = unitDefValues?.targetOutput ?: getDefaultProducing(UnitType.ENGINE, eng.ratedAcPower) ?: 0.0,
        produce = 0.0,
        heatUpTimeInTick = 5,
        hasError = unitDefValues?.hasError ?: false,
        isStarted = false,
    )


    private fun toBatteryUnit(
        bty: BatteryData,
        unitDefValues: DslUnit?,
        defVal: ConstValues
    ) = Battery(
        batteryId = bty.batteryId,
        ratedAcPower = bty.ratedAcPower,
        target = unitDefValues?.targetOutput ?: getDefaultProducing(UnitType.BATTERY, bty.ratedAcPower) ?: 0.0,
        constants = defVal,
        charge = 90.0,
        hasError = unitDefValues?.hasError ?: false
    )

    private fun getConstValues(unitType: UnitType, unitSubType: UnitSubType, ratedAcPower: Double? = null): ConstValues{ // TODO kivenni acPower-t
        val configConst = config?.typeConfig?.get(Pair(unitType, unitSubType))
        val constValues = ConstByType.get(Pair(unitType, unitSubType))
        return CustomValues(
                UP_POWER_CONTROL_PER_TICK = ratedAcPower ?: configConst?.UP_POWER_CONTROL_PER_TICK ?: constValues.UP_POWER_CONTROL_PER_TICK,
                DOWN_POWER_CONTROL_PER_TICK = ratedAcPower ?: configConst?.DOWN_POWER_CONTROL_PER_TICK ?: constValues.DOWN_POWER_CONTROL_PER_TICK,
                READ_FREQUENCY = configConst?.READ_FREQUENCY ?: constValues.READ_FREQUENCY,
                POWER_CONTROL_REACTION_TIME = configConst?.POWER_CONTROL_REACTION_TIME ?: constValues.POWER_CONTROL_REACTION_TIME,
                TIME_ACCURACY = configConst?.TIME_ACCURACY ?: constValues.TIME_ACCURACY,
                PRODUCE_ACCURACY = configConst?.PRODUCE_ACCURACY ?: constValues.PRODUCE_ACCURACY
            )
    }

    private fun getDefaultProducing(unitType: UnitType, ratedAcPower: Double): Double?{
        val defProd = config?.defaultProduceConfig?.get(unitType)
        return if (defProd == null)
            null
        else
            ratedAcPower.times(defProd)
    }

    private fun getDefValues(unitType: UnitType, id: Int): Pair<DslUnit?, List<DslTask>?> {
        val defValues = config?.unitConfig?.get(Pair(unitType, id))
        val tasks = config?.unitTasksConfig?.get(Pair(unitType, id))
        return Pair(defValues, tasks)
    }

    private fun addTaskToUnit(unitTasks: List<DslTask>?, unit: AbstractUnit) {
        unitTasks?.forEach {
            unit.taskScheduler.addTask(it.getTask(unit))
        }
    }
}
