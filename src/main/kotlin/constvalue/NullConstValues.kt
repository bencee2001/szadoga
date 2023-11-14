package constvalue

interface NullConstValues {
    val UP_POWER_CONTROL_PER_TICK: Double?
    val DOWN_POWER_CONTROL_PER_TICK: Double?
    val RATED_AC_POWER: Double?
    val READ_FREQUENCY: Int? //sec
    val POWER_CONTROL_REACTION_TIME: Int? //sec
    val TIME_ACCURACY: Double? //%
    val PRODUCE_ACCURACY: Double? //%
}