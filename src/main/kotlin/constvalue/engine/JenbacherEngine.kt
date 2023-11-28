package constvalue.engine

import constvalue.ConstValues

object JenbacherEngine: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK = -1.0 // +option
    override val DOWN_POWER_CONTROL_PER_TICK = -1.0 // +option
    override val READ_FREQUENCY = 0
    override val POWER_CONTROL_REACTION_TIME = 0
    override val TIME_ACCURACY = 0.0
    override val PRODUCE_ACCURACY = 0.01
}