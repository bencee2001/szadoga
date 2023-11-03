package scheduler

import units.AbstractUnit

abstract class Task(
    val unit: AbstractUnit,
    var holdInTick: Int
) {

    fun tick(): Int{
        holdInTick -= 1
        if(holdInTick <= 0)
            process()
        return holdInTick
    }

    abstract fun process()
}