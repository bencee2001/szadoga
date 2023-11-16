package configdsl.models

import scheduler.EndErrorTask
import scheduler.Task
import units.AbstractUnit

class DslEndErrorTask(
    override val holdInTick: Int
): DslTask {

    override fun getTask(unit: AbstractUnit): Task {
        return EndErrorTask( unit,holdInTick)
    }
}