package constvalue.loadbank

import constvalue.ConstValues

object TestLoadbank: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK: Double = 100.0 // +option
    override val DOWN_POWER_CONTROL_PER_TICK: Double = 100.0 // +option
    override val READ_FREQUENCY: Int = 0
    override val POWER_CONTROL_REACTION_TIME: Int = 0
    override val TIME_ACCURACY: Double = 0.0
    override val PRODUCE_ACCURACY: Double = 0.0
}