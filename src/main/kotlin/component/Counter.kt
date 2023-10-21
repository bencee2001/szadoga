package component

import Config
import org.json.JSONObject
import org.kalasim.Component
import org.kalasim.Event
import org.kalasim.TickTime

class Counter(defVal: Int = 0): Component() {
    var value: Int = defVal
    var step: Int = 1
    //Mutex

    override fun repeatedProcess() = sequence {
        hold(1)
        value+=step
        if(Config.COMPONENT_LOG)
            log(CntEvent(value, now))
    }

    fun read(): Int{
        return value
    }

    fun command(newVal: Int){
        step =+ newVal
    }
}

class CntEvent(val value: Int, time:TickTime): Event(time){
    override fun toJson(): JSONObject {
        return super.toJson()
            .put("value", value.toString())
    }
}