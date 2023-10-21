package component

import org.kalasim.Component

class ReaderCommander(entityList: List<Counter>): Component() {

    val entitis = entityList
    private var newVal = mapOf<Int, Int>(0 to 1, 1 to 2, 2 to 3)

    override fun repeatedProcess()= sequence<Component> {
        hold(2)
        entitis.forEachIndexed{ i, cnt ->
            println(cnt.read())
            cnt.command(newVal[i]!!)
        }

    }
}