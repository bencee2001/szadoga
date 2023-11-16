package configdsl

import configdsl.builders.DslUnitBuilder
import configdsl.models.DslTask
import configdsl.models.DslUnit
import scheduler.Task

class UnitConfigDSL {

    var defValues: DslUnit? = null
    val unitTasks = mutableListOf<DslTask>()

    fun addDefVales(init: DslUnitBuilder.() -> Unit){
        require(defValues == null) {"Default Values for Unit already set."}
        val builder = DslUnitBuilder()
        builder.init()
        defValues = builder.build()
    }

    fun addTask(test: ()->List<DslTask>){
        unitTasks.addAll(test.invoke())
    }

}