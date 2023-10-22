package park

import component.AbstractComponent

class Park(
    val parkId: Int,
    val parkName: String,
    private val componentList: List<AbstractComponent>  //TODO park-on belül csak egy fajta
){

    fun setTargetPower(target: Float){
        val targetPerComponent = target.div(componentList.size)
        componentList.forEach {
            it.command(targetPerComponent)
        }
    }

    fun getSumPower(): Float{
        var sum = 0F
        componentList.forEach {
            sum += it.read()
        }
        return sum
    }

}