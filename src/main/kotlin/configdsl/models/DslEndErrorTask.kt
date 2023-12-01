package configdsl.models

import scheduler.tasks.EndErrorTask
import scheduler.Task
import units.AbstractUnit

class DslEndErrorTask(
    override val holdInTick: Int
): DslTask {

    override fun getTask(unit: AbstractUnit): Task {
        return EndErrorTask( unit,holdInTick)
    }
}