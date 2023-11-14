package constvalue

class CustomValues(
    override val READ_FREQUENCY: Int,
    override val POWER_CONTROL_REACTION_TIME: Int,
    override val TIME_ACCURACY: Double,
    override val PRODUCE_ACCURACY: Double,
    override val UP_POWER_CONTROL_PER_TICK: Double,
    override val DOWN_POWER_CONTROL_PER_TICK: Double,
    override val RATED_AC_POWER: Double
) : ConstValues