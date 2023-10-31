package park

import units.AbstractUnit

// Lehet több fajta router egy parkban?(inverter,loadbank, engine)
// Routerek között elosztódik a target power, hogyan??

// Router szintű event logging (parkId, routerId, routerPower)/(parkId, sumRouterPower)
// log amikor fgv van hívva
class Park(
    val parkId: Int,
    val parkName: String,
    val maximumOutput: Double,
    val unitList: Map<Int,AbstractUnit>
){

    suspend fun setTargetPower(targetByUnit: Map<Int, Double>){
        targetByUnit.forEach { (i, target) ->
            unitList[i]?.command(target)
        }
    }

    fun getSumPower(): ParkPower{
        val powers = unitList.values.map { it.read() }
        val time = powers.first().tickTime
        val parkPower = powers.sumOf { it.power }
        return ParkPower(parkId, parkPower, time)
    }

}