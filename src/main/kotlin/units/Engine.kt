package units

import org.kalasim.Component
import org.kalasim.invoke
import org.kalasim.uniform

class Engine(
    engineId: Int,
    private val ratedAcPower: Double,
    private val minimumRunningPower: Double,
    engineProduceAccuracy: Double, //% - 0.2
    private var prodTarget: Double = 0.0,
    private var produce: Double = 0.0,
): AbstractUnit(engineId, UnitType.ENGINE) {

    private val produceAccuracy: Double = ratedAcPower * engineProduceAccuracy
    private var hold: Int = 0


    override fun repeatedProcess() = sequence<Component> {
        tickHold()
        setProduce()
    }

    override fun read(): UnitPower {
        return UnitPower(id, produce, now, UnitPowerMessage.PRODUCE)
    }


    override fun command(target: Double) { // not good if every 2 sec change
        prodTarget = if( target > minimumRunningPower)
            getRandomizeTarget(target)
        else
            0.0
    }

    private fun getRandomizeTarget(target: Double): Double{

        val newTarget = uniform(target-produceAccuracy, target + produceAccuracy).invoke()
        return if(newTarget > ratedAcPower)
            ratedAcPower
        else
            newTarget
    }

    private fun setProduce() {
        if (hold == 0)
            produce = prodTarget
    }

    private fun tickHold(){
        if(hold != 0){
            hold -= 1
        }
    }
}