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
    fun commandParks(targetByPowerPlantId: Map<Int, Int>){
        targetByPowerPlantId.forEach { (powerPlantId,  target)->
            val park = parkList.first{it.parkId == powerPlantId}
            park.setTargetPower(RouterLogic.getTargetByUnits(park, target.toDouble()))
        }
    }

    fun readParks(): List<ParkPower>{
        return parkList.map { it.getSumConsume() }
    }
}