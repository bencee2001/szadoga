package constvalue.inverter

import constvalue.ConstValues

class CostumInverter(
    override val READ_FREQUENCY: Int,
    override val POWER_CONTROL_REACTION_TIME: Int,
    override val INVERTER_TIME_ACCURACY: Double,
    override val INVERTER_PRODUCE_ACCURACY: Double,
    override val UP_POWER_CONTROL_PER_TICK: Double,
    override val DOWN_POWER_CONTROL_PER_TICK: Double,
    override val RATED_AC_POWER: Double
) : ConstValues