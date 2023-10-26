package powercontrol

import event.PowerControlEvent
import org.kalasim.Component
import park.Park


//Park szintű event logging (parkId, parkPower=sumRouterPower)
// tick-kénti event
class PowerController(
    private val parkList: List<Park>
){

    fun commandParks(targetByPowerPlantId: Map<Int, Int>){
        targetByPowerPlantId.forEach { (powerPlantId,  target)->
            val park = parkList.first{it.parkId == powerPlantId}
            park.setTargetPower(target.toFloat())
        }
    }

    fun readParks(): Float{
        return parkList[0].getSumPower()
    }
}