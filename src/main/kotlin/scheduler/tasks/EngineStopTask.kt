package scheduler.tasks

import scheduler.Task
import scheduler.TaskType
import units.AbstractUnit

class EngineStopTask (
    unit: AbstractUnit,
    holdInTick: Int,
): Task(unit, holdInTick, TaskType.ENGINE_START) {
    override fun process() {
        unit.targetOutput = 0.0
    }
}