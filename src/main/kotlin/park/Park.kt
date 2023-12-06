package park

import util.LogFlags
import event.ParkReadEvent
import kotlinx.coroutines.coroutineScope
import org.kalasim.Component
import org.kalasim.ComponentList
import units.AbstractUnit
import units.UnitPowerMessage
import units.UnitType
import util.eventLogging

class Park(
    val parkId: Int,
    val parkName: String,
    val maximumOutput: Double,
    val unitList: ComponentList<AbstractUnit>
): Component(){

    private var targetPower: Int = 0


    suspend fun setTargetPower(target: Int, targetsByUnitType: Map<UnitType,Map<Int, Double>>){
        targetPower = target
        targetsByUnitType.forEach{ (type, targetByUnit) ->
            val unitListByType = unitList.filter { it.type == type }.associateBy { it.id }
            targetByUnit.forEach { (i, target) ->
                coroutineScope {
                    unitListByType[i]?.command(target)
                }
            }
        }
    }

    suspend fun getSumProsume(): ParkPower{
        val powers = unitList.map { coroutineScope { it.read(LogFlags.UNIT_READ_LOG) } }
        val time = powers.first().tickTime
        val parkPower = powers.sumOf { if (it.unitPowerMessage == UnitPowerMessage.PRODUCE)
                it.power
            else
                -it.power
        }
        eventLogging(LogFlags.PARK_READ_LOG){ log(ParkReadEvent(parkId, parkName, parkPower, targetPower ,now))}
        return ParkPower(parkId,maximumOutput, parkPower, time)
    }

    suspend fun getSumProduce(): ParkPower{
        val powers = unitList.map { coroutineScope { it.read(false) } }
        val time = powers.first().tickTime
        val parkPower = powers.filter { it.unitPowerMessage == UnitPowerMessage.PRODUCE }.sumOf { it.power }
        return ParkPower(parkId, maximumOutput, parkPower, time)
    }

}