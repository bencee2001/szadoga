package scheduler

import units.AbstractUnit

class TargetSetTask(
    unit: AbstractUnit,
    holdInTick: Int,
    private val newTarget: Double
): Task(unit, holdInTick) {

    override fun process() {
        unit.targetOutput = newTarget
    }

}