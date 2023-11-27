package constvalue.inverter

import constvalue.ConstValues

object KacoInverter: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK = -1.0 // +option
    override val DOWN_POWER_CONTROL_PER_TICK = -1.0 // +option
    override val READ_FREQUENCY = 3
    override val POWER_CONTROL_REACTION_TIME = 16
    override val TIME_ACCURACY = 0.2
    override val PRODUCE_ACCURACY = 0.01
}