package constvalue.engine

import constvalue.ConstValues

object TestEngine: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK: Double = 4.0
    override val DOWN_POWER_CONTROL_PER_TICK: Double = 4.0
    override val RATED_AC_POWER: Double = 100.0
    override val READ_FREQUENCY: Int = 0
    override val POWER_CONTROL_REACTION_TIME: Int = 0 // TODO akár heat up time
    override val TIME_ACCURACY: Double = 0.0
    override val PRODUCE_ACCURACY: Double = 0.2
}