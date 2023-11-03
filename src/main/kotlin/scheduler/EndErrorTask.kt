package scheduler

import units.AbstractUnit

class EndErrorTask(
    unit: AbstractUnit,
    holdInTick: Int
): Task(unit, holdInTick) {

    override fun process() {
        unit.hasError = true
    }

}