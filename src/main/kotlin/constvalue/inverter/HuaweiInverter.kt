package constvalue.inverter

object HuaweiInverterConst: InverterConst {
    override val POWER_CONTROL_PER_TICK = 5.0
    override val READ_FREQUENCY = 5  //sec
    override val POWER_CONTROL_REACTION_TIME = 4  //sec
    override val INVERTER_TIME_ACCURACY = 0.2
    override val INVERTER_PRODUCE_ACCURACY = 0.1
}