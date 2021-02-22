plugins {
    kotlin("jvm")
}

group = "ru.tetraquark.ksp.test.processor"
version = "1.0"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:kotlinpoet:1.7.2")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.4.30-1.0.0-alpha02")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}
