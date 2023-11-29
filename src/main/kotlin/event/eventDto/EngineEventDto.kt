package event.eventDto

import event.EngineReadEvent
import event.InverterReadEvent
import units.UnitType

class EngineEventDto(
    val id: Int,
    val unitType: UnitType,
    val minPower: Int,
    val maxPower: Int,
    val power: Int,
    val currentTarget: Int,
    val time: Int
){
    constructor(event: EngineReadEvent): this(
        id = event.unitId,
        unitType = event.unitType,
        minPower = event.minPower,
        maxPower = event.maxPower,
        power = event.power,
        currentTarget = event.currentTarget,
        time = event.time.value.toInt()
    )
}