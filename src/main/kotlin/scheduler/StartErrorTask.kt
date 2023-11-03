package scheduler

import units.AbstractUnit

class StartErrorTask(
    unit: AbstractUnit,
    holdInTick: Int
): Task(unit, holdInTick) {

    override fun process() {
        unit.hasError = true
    }

}