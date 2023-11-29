package constvalue.inverter

import constvalue.ConstValues

object SMAManagerInverter: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK = -1.0 // +option
    override val DOWN_POWER_CONTROL_PER_TICK = -1.0 // +option
    override val READ_FREQUENCY = 2
    override val POWER_CONTROL_REACTION_TIME = 2
    override val TIME_ACCURACY = 4.0
    override val PRODUCE_ACCURACY = 0.0
}