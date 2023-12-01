package configdsl.builders

import configdsl.models.DslConstValues
import constvalue.NullConstValues

class DslConstValuesBuilder: NullConstValues {
    override var UP_POWER_CONTROL_PER_TICK: Double? = null
    override var DOWN_POWER_CONTROL_PER_TICK: Double? = null
    override var READ_FREQUENCY: Int? = null
    override var POWER_CONTROL_REACTION_TIME: Int? = null
    override var TIME_ACCURACY: Double? = null
    override var TARGET_ACCURACY: Double? = null

    fun build(): DslConstValues {
        return DslConstValues(
            UP_POWER_CONTROL_PER_TICK,
            DOWN_POWER_CONTROL_PER_TICK,
            READ_FREQUENCY,
            POWER_CONTROL_REACTION_TIME,
            TIME_ACCURACY,
            TARGET_ACCURACY
        )
    }
}