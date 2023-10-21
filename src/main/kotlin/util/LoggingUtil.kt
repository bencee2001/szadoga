package util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun getLogger(clazz: Class<*>): Logger = LoggerFactory.getLogger(clazz)

inline fun <reified T> getLogger(): Logger = getLogger(T::class.java)

fun Any.getLogger(): Logger = getLogger(this::class.java)