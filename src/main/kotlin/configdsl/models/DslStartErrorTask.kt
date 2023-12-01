package configdsl.models

import scheduler.tasks.StartErrorTask
import scheduler.Task
import units.AbstractUnit

data class DslStartErrorTask(
    override val holdInTick: Int
): DslTask {
    override fun getTask(unit: AbstractUnit): Task {
        return StartErrorTask(unit, holdInTick)
    }
}