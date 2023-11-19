package park

import kotlinx.coroutines.coroutineScope
import units.AbstractUnit
import units.Battery
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

    suspend fun setTargetPower(targetsByUnitType: Map<UnitType,Map<Int, Double>>){
        targetsByUnitType.forEach{ (type, targetByUnit) ->
            val unitListByType = unitList.filter { it.type == type }.associateBy { it.id }
            targetByUnit.forEach { (i, target) ->
                coroutineScope {
                    unitListByType[i]?.command(target)
                }
            }
        }
    }

    suspend fun getSumConsume(): ParkPower{
        val powers = unitList.map { coroutineScope { it.read() } }
        val time = powers.first().tickTime
        val parkPower = powers.sumOf { if (it.unitPowerMessage == UnitPowerMessage.PRODUCE)
                it.power
            else
                -it.power
        }
        return ParkPower(parkId, parkPower, time)
    }

    suspend fun getSumProduce(): ParkPower{
        val powers = unitList.map { coroutineScope { it.read() } }
        val time = powers.first().tickTime
        val parkPower = powers.filter { it.unitPowerMessage == UnitPowerMessage.PRODUCE }.sumOf { it.power }
        return ParkPower(parkId, parkPower, time)
    }

    fun isParkHaveBattery(): Boolean{
        return unitList.any { it.type == UnitType.BATTERY }
    }

    fun getNotFullBatteries(): List<Battery>{
        val batteries = unitList.filter { it.type == UnitType.BATTERY }
        val realBatteries = batteries.map { it as Battery }
        return realBatteries.filter { !it.isFull() }
    }

    fun isBatteriesFull(): Boolean{
        val batteries = unitList.filter { it.type == UnitType.BATTERY }
        val realBatteries = batteries.map { it as Battery }
        return realBatteries.all { it.isFull() }
    }
}