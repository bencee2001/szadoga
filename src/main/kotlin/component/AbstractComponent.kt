package component

import Config
import model.ComponentType
import org.kalasim.Component

abstract class AbstractComponent(
    protected val id: Int,
    protected val type: ComponentType
): Component() {

    abstract fun read(): Float
    abstract fun command(target: Float)

}