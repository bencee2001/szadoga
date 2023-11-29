package event.eventDto

import event.BatteryReadEvent
import units.UnitType

class BatteryEventDto(
    val id: Int,
    val unitType: UnitType,
    val minPower: Int,
    val maxPower: Int,
    val minCharge: Int,
    val maxCharge: Int,
    val charge: Int,
    val prosume: Int,
    val currentTarget: Int,
    val time: Int
){
    constructor(event: BatteryReadEvent): this(
        id = event.id,
        unitType = event.unitType,
        minPower = event.minPower,
        maxPower = event.maxPower,
        minCharge = event.minCharge,
        maxCharge = event.maxCharge,
        charge = event.charge,
        prosume = event.prosume,
        currentTarget = event.currentTarget,
        time = event.time.value.toInt()
    )
}