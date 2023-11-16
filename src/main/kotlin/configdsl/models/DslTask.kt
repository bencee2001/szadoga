package configdsl.models

import scheduler.Task
import units.AbstractUnit

interface DslTask {
    val holdInTick: Int

    fun getTask(unit: AbstractUnit): Task
}