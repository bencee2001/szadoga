package scheduler.tasks

import scheduler.Task
import scheduler.TaskType
import units.AbstractUnit

class EngineStartTask(
    unit: AbstractUnit,
    holdInTick: Int,
    private val newTarget: Double
): Task(unit, holdInTick, TaskType.ENGINE_START) {

    override fun process() {
        unit.targetOutput = newTarget
    }

}