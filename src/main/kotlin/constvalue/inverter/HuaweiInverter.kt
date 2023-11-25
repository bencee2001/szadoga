package constvalue.inverter

import constvalue.ConstValues

object HuaweiInverter: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK = 2000.0
    override val DOWN_POWER_CONTROL_PER_TICK = 20.0
    override val READ_FREQUENCY = 5  //sec
    override val POWER_CONTROL_REACTION_TIME = 4  //sec
    override val TIME_ACCURACY = 0.2
    override val PRODUCE_ACCURACY = 0.01
}