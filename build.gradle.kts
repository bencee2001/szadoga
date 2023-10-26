

plugins {
    kotlin("jvm") version "1.8.10"
}

group = "hu.ewiser"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.holgerbrandl:kalasim:0.11.5")
    implementation("org.jetbrains.kotlinx:dataframe:0.12.0")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}
