package scheduler.tasks

import scheduler.Task
import scheduler.TaskType
import units.AbstractUnit

class EndErrorTask(
    unit: AbstractUnit,
    holdInTick: Int
): Task(unit, holdInTick, TaskType.END_ERROR) {

    override fun process() {
        unit.hasError = false
    }

}