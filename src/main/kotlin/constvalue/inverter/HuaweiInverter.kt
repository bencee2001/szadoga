package constvalue.inverter

object HuaweiInverterConst: InverterConst {
    override val MAX_POWER_OUTPUT = 100F
    override val POWER_CONTROL_PER_TICK = 5F
    override val READ_FREQUENCY = 5  //sec
    override val POWER_CONTROL_REACTION_TIME = 4  //sec
}