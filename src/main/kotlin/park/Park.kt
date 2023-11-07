package park

import units.AbstractUnit
import units.UnitPowerMessage
import units.UnitType

// Lehet több fajta router egy parkban?(inverter,loadbank, engine)
// Routerek között elosztódik a target power, hogyan??

// Router szintű event logging (parkId, routerId, routerPower)/(parkId, sumRouterPower)
// log amikor fgv van hívva
class Park(
    val parkId: Int,
    val parkName: String,
    val maximumOutput: Double,
    val unitList: List<AbstractUnit>
){

    fun setTargetPower(targetsByUnitType: Map<UnitType,Map<Int, Double>>){
        targetsByUnitType.forEach{ (type, targetByUnit) ->
            val unitListByType = unitList.filter { it.type == type }.associateBy { it.id }
            targetByUnit.forEach { (i, target) ->
                unitListByType[i]?.command(target)
            }
        }
    }

    fun getSumConsume(): ParkPower{
        val powers = unitList.map { it.read() }
        val time = powers.first().tickTime
        val parkPower = powers.sumOf { if (it.unitPowerMessage == UnitPowerMessage.PRODUCE)
                it.power
            else
                -it.power
        }
        return ParkPower(parkId, parkPower, time)
    }

    fun getSumProduce(): ParkPower{
        val powers = unitList.map { it.read() }
        val time = powers.first().tickTime
        val parkPower = powers.filter { it.unitPowerMessage == UnitPowerMessage.PRODUCE }.sumOf { it.power }
        return ParkPower(parkId, parkPower, time)
    }
}