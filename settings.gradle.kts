pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "1.4.30-1.0.0-alpha02"
        kotlin("jvm") version "1.4.30"
    }
    repositories {
        gradlePluginPortal()
        google()
    }
}

include(":ksp-processor")
include(":jvm-sample")
//include(":multiplatform-sample") // KMM doesn't support yet :(
