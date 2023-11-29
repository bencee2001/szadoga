package constvalue.battery

import constvalue.ConstValues

object TestBattery: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK: Double = 400.0
    override val DOWN_POWER_CONTROL_PER_TICK: Double = 200.0
    override val READ_FREQUENCY: Int = 2
    override val POWER_CONTROL_REACTION_TIME: Int = 3
    override val TIME_ACCURACY: Double = 0.1
    override val PRODUCE_ACCURACY: Double = 0.1
}