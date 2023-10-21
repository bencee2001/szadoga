package util

fun eventLogging( block: () -> Unit){
    if(Config.COMPONENT_LOG){
        block()
    }
}