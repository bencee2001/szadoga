package component

import model.ComponentType
import org.kalasim.Component

class Loadbank(
    loadbankId: Int,
    private var prosume: Float = 0F,
    private val canStart: Boolean = false,
    //TODO kell-e default heat
): AbstractComponent(loadbankId, type = ComponentType.LOADBANK)  {

    override fun repeatedProcess() = sequence<Component> {

    }

    override fun read(): Float {
         return prosume
    }

    override fun command(target: Float) {
        TODO("Not yet implemented")
    }
}