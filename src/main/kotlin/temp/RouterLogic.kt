package temp

import park.Park
import units.Inverter
import units.Loadbank

import units.UnitType

class RouterLogic {
    companion object{

        /**
         * TODO
         *
         * @return Map<UnitId, unitTarget>
         */
        fun getTargetByUnits(park: Park, target: Double): Map<UnitType,Map<Int, Double>>{
            val targetVal = if(park.maximumOutput < target) park.maximumOutput else target
            val currentProsume = park.getSumProduce()
            println(currentProsume.power)
            val targetDiff = targetVal - currentProsume.power
            val targetByUnitId = mutableMapOf<UnitType,Map<Int, Double>>()
            targetByUnitId[UnitType.INVERTER] = getInverterTargets(park, targetVal)
            val test = getLoadbankTargets(park, targetDiff.times(-1))
            println(test.values)
            targetByUnitId[UnitType.LOADBANK] = test
            return targetByUnitId
        }

        private fun getInverterTargets(park: Park, targetVal: Double): Map<Int, Double> {
            val inverters = park.unitList.filter { unit -> unit.type == UnitType.INVERTER }.map { unit -> unit as Inverter }
            val inverterSumOutput = inverters.sumOf { it.constants.RATED_AC_POWER }
            val avg = targetVal.div(inverterSumOutput)
            return inverters.associate { it.id to it.constants.RATED_AC_POWER.times(avg) }
        }

        private fun getLoadbankTargets(park: Park, targetDiff: Double): Map<Int, Double> {
            val loadbanks = park.unitList.filter { unit -> unit.type == UnitType.LOADBANK }.map { unit -> unit as Loadbank }
            val loadbankControlValues = mutableMapOf<Int, Double>()
            if(targetDiff > 5){
                val loadbankWithConsume = loadbanks.map { it to it.read() }
                val sortedByConsume = loadbankWithConsume.sortedBy { it.second.power }.map { it.first }
                var buffTargetDiff = targetDiff
                sortedByConsume.forEach {
                    val target: Double = calculateLoadbankTarget(buffTargetDiff, it.constants.RATED_AC_POWER)
                    loadbankControlValues[it.id] = target
                    if(!it.canNextStart())
                        return@forEach
                    buffTargetDiff -= it.constants.RATED_AC_POWER
                }
            }else{
                loadbankControlValues.putAll(loadbanks.associate{ it.id to 0.0 })
            }
            return loadbankControlValues
        }

        private fun calculateLoadbankTarget(targetDiff: Double, maxOutput: Double): Double {
            return if( targetDiff > maxOutput )
                maxOutput
            else
                targetDiff
        }


    }
}