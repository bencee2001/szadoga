package temp

import park.Park
import units.Inverter

import units.UnitType

class RouterLogic {
    companion object{

        /**
         * TODO
         *
         * @return Map<UnitId, unitTarget>
         */
        fun getTargetByUnits(park: Park, target: Double): Map<Int, Double>{
            val targetVal = if(park.maximumOutput < target) park.maximumOutput else target
            val currentProsume = park.getSumPower()
            val targetDiff = targetVal - currentProsume.power
            val targetByUnitId = mutableMapOf<Int, Double>()
            targetByUnitId.putAll(getInverterTargets(park, targetVal))
            /*if(targetDiff < 0){
                targetByUnitId.putAll(getLoadbankTargets(park, targetVal))
            }*/
            return targetByUnitId
        }

        private fun getInverterTargets(park: Park, targetVal: Double): Map<Int, Double> {
            val inverters = park.unitList.filter { (_, unit) -> unit.type == UnitType.INVERTER }.map { (_, unit) -> unit as Inverter }
            val inverterSumOutput = inverters.sumOf { it.constants.RATED_AC_POWER }
            val avg = targetVal.div(inverterSumOutput)
            return inverters.associate { it.id to it.constants.RATED_AC_POWER.times(avg) }
        }

        /*private fun getLoadbankTargets(park: Park, targetDiff: Double): Map<Int, Double> {
            val loadbanks = park.unitList.filter { (_, unit) -> unit.type == UnitType.LOADBANK }.map { (_, unit) -> unit as Loadbank }
            return loadbanks.associate { it.id to 0.0 }
        }*/


    }
}