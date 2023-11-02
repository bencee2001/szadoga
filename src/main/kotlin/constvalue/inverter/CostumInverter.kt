package constvalue.inverter

class CostumInverter(
    override val POWER_CONTROL_PER_TICK: Double,
    override val READ_FREQUENCY: Int,
    override val POWER_CONTROL_REACTION_TIME: Int,
    override val INVERTER_TIME_ACCURACY: Double,
    override val INVERTER_PRODUCE_ACCURACY: Double
) : InverterConst