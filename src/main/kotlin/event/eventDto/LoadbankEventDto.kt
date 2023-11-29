package event.eventDto

import event.LoadbankReadEvent
import units.UnitPowerMessage
import units.UnitType

class LoadbankEventDto(
    val id: Int,
    val unitType: UnitType,
    val minConsume: Int,
    val maxConsume: Int,
    val consume: Int,
    val unitPowerMessage: UnitPowerMessage,
    val temperature: Int,
    val currentTarget: Int,
    val temperatureTarget: Int,
    val time: Int
) {

    constructor(event: LoadbankReadEvent): this(
        id = event.id,
        unitType = event.unitType,
        minConsume = event.minConsume,
        maxConsume = event.maxConsume,
        consume = event.consume,
        unitPowerMessage = event.unitPowerMessage,
        temperature = event.temperature,
        temperatureTarget = event.temperatureTarget,
        currentTarget = event.currentTarget,
        time = event.time.value.toInt()
    )
}