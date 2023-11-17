package constvalue.inverter

import constvalue.ConstValues

object TestInverter: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK = 0.3
    override val DOWN_POWER_CONTROL_PER_TICK = 0.3
    override val READ_FREQUENCY = 4
    override val POWER_CONTROL_REACTION_TIME = 5
    override val TIME_ACCURACY = 0.25
    override val PRODUCE_ACCURACY = 0.12
}