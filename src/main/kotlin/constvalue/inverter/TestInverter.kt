package constvalue.inverter

object TestInverter: InverterConst {
    override val POWER_CONTROL_PER_TICK = 3.0
    override val READ_FREQUENCY = 4
    override val POWER_CONTROL_REACTION_TIME = 5
    override val INVERTER_TIME_ACCURACY = 0.25
    override val INVERTER_PRODUCE_ACCURACY = 0.12
}