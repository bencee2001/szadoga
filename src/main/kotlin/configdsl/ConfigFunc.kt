package configdsl

fun config(initializer: ConfigDSL.() -> Unit): ConfigDSL{
    return ConfigDSL().apply(initializer)
}