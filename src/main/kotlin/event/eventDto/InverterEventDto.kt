package event.eventDto

import event.InverterReadEvent
import org.checkerframework.checker.units.qual.Current
import units.UnitType

class InverterEventDto(
    val id: Int,
    val unitType: UnitType,
    val minPower: Int,
    val maxPower: Int,
    val power: Int,
    val currentTarget: Int,
    val time: Int
){
    constructor(event: InverterReadEvent): this(
        id = event.unitId,
        unitType = event.unitType,
        minPower = event.minPower,
        maxPower = event.maxPower,
        power = event.power,
        currentTarget = event.currentTarget,
        time = event.time.value.toInt()
    )
}