package constvalue.inverter

import model.InverterType

object ConstByType {
    val map = mapOf(
        InverterType.HUAWEI to HuaweiInverterConst,
        InverterType.TEST to TestInverter
    )
}