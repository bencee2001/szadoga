package powercontrol

import park.Park
import park.ParkPower
import router.RouterInterface
import router.RouterLogic

class PowerController(
    private val parkList: List<Park>
){

    private val routerLogic: RouterInterface = RouterLogic

    suspend fun commandParks(targetByPowerPlantId: Map<Int, Int>){
        targetByPowerPlantId.forEach { (powerPlantId,  target)->
            val park = parkList.first{it.parkId == powerPlantId}
            park.setTargetPower(target,routerLogic.getTargetByUnits(park, target.toDouble()))
        }
    }

    suspend fun readParks(): List<ParkPower>{
        return parkList.map {
            it.getSumProsume()
        }
    }

    suspend fun readParksById(powerPlantIds: List<Int>): List<ParkPower>?{
        val parks = parkList.filter { it.parkId in powerPlantIds }
        if(parks.isEmpty())
            return null
        return parks.map { it.getSumProsume() }
    }

    fun getMaxOutputByParkId(): Map<Int, Double>{
        return parkList.associate { it.parkId to it.maximumOutput }
    }

    fun getParks(): List<Park>{
        return parkList
    }
}