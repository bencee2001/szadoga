package constvalue.inverter

import constvalue.ConstValues

object HuaweiInverterConst: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK = 0.5
    override val DOWN_POWER_CONTROL_PER_TICK = 0.3
    override val RATED_AC_POWER = 50.0
    override val READ_FREQUENCY = 5  //sec
    override val POWER_CONTROL_REACTION_TIME = 4  //sec
    override val TIME_ACCURACY = 0.2
    override val PRODUCE_ACCURACY = 0.1
}