package scheduler

import units.AbstractUnit

abstract class Task(
    val unit: AbstractUnit,
    var holdInTick: Int,
    val type: TaskType
) {

    fun tick(): Int{
        holdInTick -= 1
        if(holdInTick <= 0)
            process()
        return holdInTick
    }

    protected abstract fun process()
}