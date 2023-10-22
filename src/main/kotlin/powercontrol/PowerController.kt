package powercontrol

import event.PowerControlEvent
import org.kalasim.Component
import park.Park

class PowerController(
    private val parkList: List<Park>
): Component() {

    override fun repeatedProcess() = sequence<Component> {
        hold(2)
        parkList.forEach{
            log(PowerControlEvent(it.parkId, it.getSumPower(), now))
        }
    }
}