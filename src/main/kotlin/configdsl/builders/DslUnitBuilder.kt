package configdsl.builders

import configdsl.models.DslUnit

class DslUnitBuilder {
    var targetOutput: Double? = null
    var hasError: Boolean? = null

    fun build(): DslUnit {
        return DslUnit(targetOutput, hasError)
    }
}