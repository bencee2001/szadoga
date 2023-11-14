package constvalue

class DslValues(
    override val UP_POWER_CONTROL_PER_TICK: Double? = null,
    override val DOWN_POWER_CONTROL_PER_TICK: Double? = null,
    override val RATED_AC_POWER: Double? = null,
    override val READ_FREQUENCY: Int? = null,
    override val POWER_CONTROL_REACTION_TIME: Int? = null,
    override val TIME_ACCURACY: Double? = null,
    override val PRODUCE_ACCURACY: Double? = null
) : NullConstValues