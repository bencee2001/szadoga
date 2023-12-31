package constvalue.loadbank

import constvalue.ConstValues

object TestLoadbank: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK: Double = 100.0
    override val DOWN_POWER_CONTROL_PER_TICK: Double = 100.0
    override val READ_FREQUENCY: Int = 0
    override val POWER_CONTROL_REACTION_TIME: Int = 0
    override val TIME_ACCURACY: Double = 0.0
    override val TARGET_ACCURACY: Double = 0.0
}