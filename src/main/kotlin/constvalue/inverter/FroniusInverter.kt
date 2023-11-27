package constvalue.inverter

import constvalue.ConstValues

object FroniusInverter: ConstValues {
    override val UP_POWER_CONTROL_PER_TICK = -1.0 // +option
    override val DOWN_POWER_CONTROL_PER_TICK = -1.0 // +option
    override val READ_FREQUENCY = 14
    override val POWER_CONTROL_REACTION_TIME = 5
    override val TIME_ACCURACY = 0.2
    override val PRODUCE_ACCURACY = 0.02  //example 4000 szab -> 5000 term
}