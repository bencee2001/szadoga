package router

import park.Park
import units.UnitType

interface RouterInterface {
    suspend fun getTargetByUnits(park: Park, target: Double): Map<UnitType, Map<Int, Double>>
}