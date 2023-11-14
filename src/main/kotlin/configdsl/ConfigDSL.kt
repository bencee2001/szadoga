package configdsl

import constvalue.NullConstValues
import model.types.UnitSubType
import units.AbstractUnit
import units.UnitType

class ConfigDSL {
    val typeConfig = mutableMapOf<Pair<UnitType, UnitSubType>, NullConstValues>()
    val unitConfig = mutableMapOf<Pair<UnitType, Int>, AbstractUnit>()
}