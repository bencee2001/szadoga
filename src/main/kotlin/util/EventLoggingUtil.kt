package util

const val PATH = "src\\main\\LogResults"

fun eventLogging(isLogging: Boolean ,block: () -> Unit){
    if(isLogging){
        block()
    }
}

object LogFlags {
    var UNIT_READ_LOG = true
    var PARK_READ_LOG = false
}


