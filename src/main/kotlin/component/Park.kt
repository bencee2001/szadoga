package component

import org.kalasim.Component

class Park(
    private val parkId: Int,
    private val parkName: String,
    private val componentList: List<AbstractComponent>  //TODO park-on belül csak egy fajta
): Component() {

    override fun repeatedProcess() = sequence<Component> {

    }

    fun setTargetPower(target: Float){
        val targetPerComponent = target.div(componentList.size)
        componentList.forEach {
            it.command(targetPerComponent)
        }
    }

    fun getSumPower() {
        var sum = 0F
        componentList.forEach {
            sum =+ it.read()
        }
    }

}