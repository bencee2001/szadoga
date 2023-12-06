package constvalue

interface ConstValues: NullConstValues {
    override val UP_POWER_CONTROL_PER_TICK: Double
    override val DOWN_POWER_CONTROL_PER_TICK: Double
    override val READ_FREQUENCY: Int //sec
    override val POWER_CONTROL_REACTION_TIME: Int //sec
    override val TIME_ACCURACY: Double //%
    override val TARGET_ACCURACY: Double //%
}