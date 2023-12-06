package configdsl

import configdsl.builders.DslConstValuesBuilder
import configdsl.models.DslTask
import configdsl.models.DslUnit
import constvalue.NullConstValues
import model.types.UnitSubType
import units.UnitType

typealias DefaultProduceConfig = MutableMap<UnitType, Double>
typealias TypeConfig = MutableMap<Pair<UnitType, UnitSubType>, NullConstValues>
typealias UnitConfig = MutableMap<Pair<UnitType, Int>, DslUnit>
typealias UnitTasksConfig = MutableMap<Pair<UnitType, Int>, MutableList<DslTask>>

class ConfigDSL {
    val defaultProduceConfig: DefaultProduceConfig = mutableMapOf()
    val typeConfig: TypeConfig = mutableMapOf()
    val unitConfig: UnitConfig = mutableMapOf()
    val unitTasksConfig: UnitTasksConfig = mutableMapOf()

    fun addDefaultProduceConfig(unitType: UnitType, defaultPercentage: Double){
        defaultProduceConfig[unitType] = defaultPercentage
    }

    fun addTypeConfig(unitType: UnitType, unitSubType: UnitSubType ,init: DslConstValuesBuilder.() -> Unit){
        val builder = DslConstValuesBuilder()
        builder.init()
        typeConfig[Pair(unitType, unitSubType)] = builder.build()
    }

    fun addUnitConfig(unitType: UnitType, unitId: Int, block: UnitConfigDSL.() -> Unit){
        val unitConfigDsl = UnitConfigDSL()
        unitConfigDsl.block()
        val defVal = unitConfigDsl.defValues
        if(defVal != null)
            unitConfig[Pair(unitType, unitId)] = defVal
        val taskList = mutableListOf<DslTask>()
        unitConfigDsl.unitTasks.forEach {
            taskList.add(it)
        }
        unitTasksConfig[Pair(unitType, unitId)] = taskList

    }
}