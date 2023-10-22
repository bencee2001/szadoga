package util

fun unitEventLogging(block: () -> Unit){
    if(Config.UNIT_LOG){
        block()
    }
}


