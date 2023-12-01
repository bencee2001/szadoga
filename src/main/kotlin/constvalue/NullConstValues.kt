package constvalue

interface NullConstValues {
    val UP_POWER_CONTROL_PER_TICK: Double?
    val DOWN_POWER_CONTROL_PER_TICK: Double?
    val READ_FREQUENCY: Int? //sec
    val POWER_CONTROL_REACTION_TIME: Int? //sec
    val TIME_ACCURACY: Double? //sec
    val TARGET_ACCURACY: Double? //%
}