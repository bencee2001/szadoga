package constvalue.inverter

import constvalue.ConstValues

object SMAManagerInverter: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK = -1.0
    override val DOWN_POWER_CONTROL_PER_TICK = -1.0
    override val READ_FREQUENCY = 2
    override val POWER_CONTROL_REACTION_TIME = 2
    override val TIME_ACCURACY = 4.0
    override val TARGET_ACCURACY = 0.0
}