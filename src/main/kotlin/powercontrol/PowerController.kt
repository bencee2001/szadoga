package powercontrol

import park.Park
import park.ParkPower
import temp.RouterLogic


//Park szintű event logging (parkId, parkPower=sumRouterPower)
// tick-kénti event
class PowerController(
    private val parkList: List<Park>
){

    /**
     *
     * @param targetByPowerPlantId Map<powerPlantId, powerPlantTarget>
     */
    suspend fun commandParks(targetByPowerPlantId: Map<Int, Int>){
        targetByPowerPlantId.forEach { (powerPlantId,  target)->
            val park = parkList.first{it.parkId == powerPlantId}
            park.setTargetPower(target,RouterLogic.getTargetByUnits(park, target.toDouble()))
        }
    }

    suspend fun readParks(): List<ParkPower>{
        return parkList.map {
            it.getSumConsume()
        }
    }

    suspend fun readParksById(powerPlantIds: List<Int>): List<ParkPower>?{
        val parks = parkList.filter { it.parkId in powerPlantIds }
        if(parks.isEmpty())
            return null
        return parks.map { it.getSumConsume() }
    }

    fun getMaxOutputByParkId(): Map<Int, Double>{
        return parkList.associate { it.parkId to it.maximumOutput }
    }
}