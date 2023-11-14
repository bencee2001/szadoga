package constvalue

import constvalue.battery.TestBattery
import constvalue.engine.TestEngine
import constvalue.inverter.HuaweiInverter
import constvalue.inverter.TestInverter
import constvalue.loadbank.TestLoadbank
import model.types.*
import units.UnitType

object ConstByType {
    private val map = mapOf<Pair<UnitType, UnitSubType>, ConstValues>(
        Pair(UnitType.INVERTER,InverterType.HUAWEI) to HuaweiInverter,
        Pair(UnitType.INVERTER,InverterType.TEST) to TestInverter,

        Pair(UnitType.ENGINE,EngineType.TEST) to TestEngine,

        Pair(UnitType.LOADBANK,LoadbankType.TEST) to TestLoadbank,

        Pair(UnitType.BATTERY,BatteryType.TEST) to TestBattery,
    )

    fun get(key: Pair<UnitType, UnitSubType>): ConstValues{
        val constVal = map[key]
        require(constVal != null){"No such UnitType(${key.first}), UnitSubType(${key.second}) variation."}
        return constVal
    }
}