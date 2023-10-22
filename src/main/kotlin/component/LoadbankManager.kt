package component

import model.ComponentType
import org.kalasim.Component

class LoadbankManager(
    controllerId: Int,
    private val maxPowerConsume: Float,
    private val initiateNumber: Int,
    private val loadbanks: List<Loadbank>
):AbstractComponent(controllerId, type = ComponentType.LOADBANKCONTROLLER) {

    private var currentConsume: Float


    init {
        var consume = 0F
        loadbanks.forEach {
            consume += it.read()
        }
        currentConsume = consume

    }

    override fun repeatedProcess() = sequence<Component> {
        hold(5)



    }

    override fun read(): Float {
        TODO("Not yet implemented")
    }

    override fun command(target: Float) {
        TODO("Not yet implemented")
    }

    private fun isConsumingOnPercentage(percantage: Double, index: Int){

    }
}
