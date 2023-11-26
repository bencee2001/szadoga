package util

const val PATH = "src\\main\\LogResults"

fun eventLogging(isLogging: Boolean ,block: () -> Unit){
    if(isLogging){
        block()
    }
}


