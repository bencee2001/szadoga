package util

const val PATH = "src\\main\\LogResults"

fun eventLogging(isLoggingEnabled: Boolean, block: () -> Unit){
    if(isLoggingEnabled)
        block()
}

object LogFlags {
    var UNIT_READ_LOG = true
    var PARK_READ_LOG = false
    val testasd = null
}


