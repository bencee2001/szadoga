package event.eventDto

import event.ParkReadEvent

class ParkEventDto (
    val powerPlantId: Int,
    val powerPlantName: String,
    val producedPower: Int,
    val targetPower: Int,
    val time: Int,
){
    constructor(event: ParkReadEvent): this(
        powerPlantId = event.powerPlantId,
        powerPlantName = event.powerPlantName,
        producedPower = event.producedPower.toInt(),
        targetPower = event.targetPower,
        time = event.time.value.toInt()
    )
}