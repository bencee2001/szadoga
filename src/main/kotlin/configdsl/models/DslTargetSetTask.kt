package configdsl.models

import scheduler.tasks.TargetSetTask
import scheduler.Task
import units.AbstractUnit

class DslTargetSetTask(
    private val newTarget: Double,
    override val holdInTick: Int
): DslTask {

    override fun getTask(unit: AbstractUnit): Task {
        return TargetSetTask(unit, holdInTick, newTarget)
    }

}