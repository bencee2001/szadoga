package util

fun eventLogging(isLogging: Boolean ,block: () -> Unit){
    if(isLogging){
        block()
    }
}


