package temp

import park.Park
import units.*

class RouterLogic {
    companion object{

        /**
         * TODO
         *
         * @return Map<UnitId, unitTarget>
         */
        fun getTargetByUnits(park: Park, target: Double): Map<UnitType, Map<Int, Double>> {
            var targetVal = if (park.maximumOutput < target) park.maximumOutput else target
            val currentProsume = park.getSumProduce()
            val targetDiff = targetVal - currentProsume.power
            targetVal += getNewTargetValByBattery(park)
            val targetByUnitId = mutableMapOf<UnitType, Map<Int, Double>>()
            targetByUnitId[UnitType.INVERTER] = getInverterTargets(park, targetVal)
            targetByUnitId[UnitType.LOADBANK] = getLoadbankTargets(park, targetDiff.times(-1))
            targetByUnitId[UnitType.ENGINE] = getEngineTargets(park, targetVal)
            targetByUnitId[UnitType.BATTERY] = getBatteryTargets(park, targetDiff)
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

        private fun getEngineTargets(park: Park, targetVal: Double): Map<Int, Double> {
            val engines = park.unitList.filter { unit -> unit.type == UnitType.ENGINE }.map { unit -> unit as Engine }
            return engines.associate { it.id to targetVal }
        }

        private fun getBatteryTargets(park: Park, targetDiff: Double): Map<Int, Double> {
            val batteries = park.unitList.filter { unit -> unit.type == UnitType.BATTERY }.map { unit -> unit as Battery }
            val notFullBatteries = batteries.filter { !it.isFull() }
            val targets = mutableMapOf<Int, Double>()
            if(targetDiff > 0.0){
                val fullBatteries = batteries.filter { it.isFull() }
                targets.putAll(fullBatteries.associate { it.id to -it.constants.DOWN_POWER_CONTROL_PER_TICK })
            }
            targets.putAll(notFullBatteries.associate { it.id to it.constants.UP_POWER_CONTROL_PER_TICK })
            return targets
        }

        private fun calculateLoadbankTarget(targetDiff: Double, maxOutput: Double): Double {
            return if( targetDiff > maxOutput )
                maxOutput
            else
                targetDiff
        }

        private fun getNewTargetValByBattery(park: Park): Double {
            val batteries = park.unitList.filter { it.type == UnitType.BATTERY }.map { it as Battery }
            val notFullBatteries = batteries.filter { !it.isFull() }
            return notFullBatteries.sumOf { it.constants.UP_POWER_CONTROL_PER_TICK }
        }
    }
}