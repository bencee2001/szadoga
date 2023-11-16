import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    kotlin("jvm") version "1.8.10"
    application
}

group = "hu.ewiser"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    //implementation("com.github.holgerbrandl:kalasim:0.11.6")
    implementation("org.jetbrains.kotlinx:dataframe:0.12.0")
    //implementation("io.insert-koin:koin-logger-slf4j:3.1.2")
    implementation(files("libs\\kalasim-0.11.5.jar"))
    // https://mvnrepository.com/artifact/com.thinkinglogic.builder/kotlin-builder-annotation

    api("io.insert-koin:koin-core:3.3.3")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("src/main/kotlin/Main.kt")
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}
