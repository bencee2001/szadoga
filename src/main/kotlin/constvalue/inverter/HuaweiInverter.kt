package constvalue.inverter

object HuaweiInverterConst: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK = 5.0
    override val DOWN_POWER_CONTROL_PER_TICK = 3.0
    override val RATED_AC_POWER = 50.0
    override val READ_FREQUENCY = 5  //sec
    override val POWER_CONTROL_REACTION_TIME = 4  //sec
    override val INVERTER_TIME_ACCURACY = 0.2
    override val INVERTER_PRODUCE_ACCURACY = 0.1
}