package scheduler

import units.AbstractUnit

class EndErrorTask(
    unit: AbstractUnit,
    holdInTick: Int
): Task(unit, holdInTick, TaskType.END_ERROR) {

    override fun process() {
        unit.hasError = true
    }

}