package park

import units.AbstractUnit

// Lehet több fajta router egy parkban?(inverter,loadbank, engine)
// Routerek között elosztódik a target power, hogyan??

// Router szintű event logging (parkId, routerId, routerPower)/(parkId, sumRouterPower)
// log amikor fgv van hívva
class Park(
    val parkId: Int,
    val parkName: String,
    val maximumOutput: Float,
    private val unitList: List<AbstractUnit>
){

    fun setTargetPower(target: Float){
        val targetPerUnit = target.div(unitList.size)
        unitList.forEach {
            it.command(targetPerUnit)
        }
    }

    fun getSumPower(): Float{
        var sum = 0F
        unitList.forEach {
            sum += it.read()
        }
        return sum
    }

}