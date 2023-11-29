package temp

import park.Park
import units.*

class RouterLogic {
    companion object {

        /**
         * TODO
         *
         * @return Map<UnitId, unitTarget>
         */
        suspend fun getTargetByUnits(park: Park, target: Double): Map<UnitType, Map<Int, Double>> {
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
            val inverters =
                park.unitList.filter { unit -> unit.type == UnitType.INVERTER }.map { unit -> unit as Inverter }
            val inverterSumOutput = inverters.sumOf { it.ratedAcPower }
            val avg = targetVal.div(inverterSumOutput)
            return inverters.associate { it.id to it.ratedAcPower.times(avg) }
        }

        private fun getLoadbankTargets(park: Park, targetDiff: Double): Map<Int, Double> {
            var target = targetDiff
            val loadbankTargets = mutableMapOf<Int, Double>()
            val loadbanks =
                park.unitList.filter { unit -> unit.type == UnitType.LOADBANK }.map { unit -> unit as Loadbank }
            if(loadbanks.isNotEmpty()) {
                val biggestLoadbankPower = loadbanks.maxBy { it.ratedAcPower }.ratedAcPower
                if (targetDiff > biggestLoadbankPower) {
                    run loop@{
                        loadbanks.sortedByDescending { it.ratedAcPower }.forEach { ld ->
                            if (ld.ratedAcPower <= target) {
                                loadbankTargets[ld.id] = ld.ratedAcPower
                                target -= ld.ratedAcPower
                                if (!ld.canNextStart()) {
                                    return@loop
                                }
                            }
                        }
                    }
                    if (loadbanks.filter { it.id in loadbankTargets.keys }.all { it.canNextStart() }) {
                        run loop@{
                            loadbanks.sortedByDescending { it.ratedAcPower }.filter { it.id !in loadbankTargets.keys }
                                .forEach { ld ->
                                    if (ld.ratedAcPower > target) {
                                        loadbankTargets[ld.id] = target
                                        target = 0.0
                                        return@loop
                                    }
                                }
                        }
                    }
                } else {
                    loadbankTargets.putAll(loadbanks.associate { it.id to 0.0 })
                }
            }
            return loadbankTargets
        }

        private fun getEngineTargets(park: Park, targetVal: Double): Map<Int, Double> {
            var target = targetVal
            val engines = park.unitList.filter { unit -> unit.type == UnitType.ENGINE }.map { unit -> unit as Engine }
            val engineTargets = mutableMapOf<Int, Double>()
            engines.filter { it.isStarted }.forEach { eng ->
                if (eng.ratedAcPower <= target) {
                    engineTargets[eng.id] = eng.ratedAcPower
                    target -= eng.ratedAcPower
                } else if (eng.getStartPower() <= target) {
                    engineTargets[eng.id] = target
                    target = 0.0
                } else {
                    engineTargets[eng.id] = 0.0
                }
            }
            engines.filter { it.id !in engineTargets.keys }.sortedByDescending { it.ratedAcPower }.forEach { eng ->
                if (eng.ratedAcPower <= target) {
                    engineTargets[eng.id] = eng.ratedAcPower
                    target -= eng.ratedAcPower
                }
            }
            engines.filter { it.id !in engineTargets.keys }.sortedByDescending { it.getStartPower() }.forEach { eng ->
                if (eng.getStartPower() < target) {
                    engineTargets[eng.id] = target
                    target = 0.0
                }
            }
            for (eng in engines.filter { it.id !in engineTargets.keys }) {
                engineTargets[eng.id] = 0.0
            }
            return engineTargets
        }

        private fun getBatteryTargets(park: Park, targetDiff: Double): Map<Int, Double> {
            val batteries =
                park.unitList.filter { unit -> unit.type == UnitType.BATTERY }.map { unit -> unit as Battery }
            val batteryTargets = mutableMapOf<Int, Double>()
            val sumBattery = batteries.sumOf { it.ratedAcPower.times(0.4) }
            if (targetDiff > sumBattery) {
                val emptyBatteries = batteries.filter { it.isEmpty() }
                val notEmptyBattery = batteries.filter { !it.isEmpty() }
                batteryTargets.putAll(notEmptyBattery.associate { it.id to -it.constants.DOWN_POWER_CONTROL_PER_TICK })
                batteryTargets.putAll(emptyBatteries.associate { it.id to 0.0 })
            }else{
                val fullBatteries = batteries.filter { it.isFull() }
                val notFullBattery = batteries.filter { !it.isFull() }
                batteryTargets.putAll(notFullBattery.associate { it.id to it.constants.UP_POWER_CONTROL_PER_TICK })
                batteryTargets.putAll(fullBatteries.associate { it.id to 0.0 })            }
            return batteryTargets
        }

        private fun calculateLoadbankTarget(targetDiff: Double, maxOutput: Double): Double {
            return if (targetDiff > maxOutput)
                maxOutput
            else
                targetDiff
        }

        private fun getNewTargetValByBattery(park: Park): Double {
            val batteries = park.unitList.filter { it.type == UnitType.BATTERY }.map { it as Battery }
            val notFullBatteries = batteries.filter { !it.isEmpty() }
            return notFullBatteries.sumOf { it.constants.UP_POWER_CONTROL_PER_TICK }
        }
    }
}