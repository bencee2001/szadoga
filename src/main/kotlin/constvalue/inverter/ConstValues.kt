package constvalue.inverter

interface ConstValues {
    val UP_POWER_CONTROL_PER_TICK: Double
    val DOWN_POWER_CONTROL_PER_TICK: Double
    val RATED_AC_POWER: Double
    val READ_FREQUENCY: Int //sec
    val POWER_CONTROL_REACTION_TIME: Int //sec
    val INVERTER_TIME_ACCURACY: Double //%
    val INVERTER_PRODUCE_ACCURACY: Double //%
}