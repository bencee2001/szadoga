package constvalue.inverter

import constvalue.ConstValues

object TestInverter: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK = -1.0 // +option
    override val DOWN_POWER_CONTROL_PER_TICK = -1.0 // +option
    override val READ_FREQUENCY = 4
    override val POWER_CONTROL_REACTION_TIME = 5
    override val TIME_ACCURACY = 1.0
    override val TARGET_ACCURACY = 0.012
}