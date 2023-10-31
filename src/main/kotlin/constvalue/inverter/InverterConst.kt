package constvalue.inverter

interface InverterConst {
    val POWER_CONTROL_PER_TICK: Double
    val READ_FREQUENCY: Int //sec
    val POWER_CONTROL_REACTION_TIME: Int //sec
    val INVERTER_TIME_ACCURACY: Double //%
    val INVERTER_PRODUCE_ACCURACY: Double //%
}