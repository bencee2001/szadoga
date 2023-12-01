package scheduler.tasks

import scheduler.Task
import scheduler.TaskType
import units.AbstractUnit

class StartErrorTask(
    unit: AbstractUnit,
    holdInTick: Int
): Task(unit, holdInTick, TaskType.START_ERROR) {

    override fun process() {
        unit.hasError = true
    }

}