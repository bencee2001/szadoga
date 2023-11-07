package constvalue

import constvalue.inverter.HuaweiInverterConst
import constvalue.inverter.TestInverter
import model.InverterType

object ConstByType {
    val map = mapOf(
        InverterType.HUAWEI to HuaweiInverterConst,
        InverterType.TEST to TestInverter
    )
}