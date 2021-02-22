plugins {
    id("com.google.devtools.ksp")
    kotlin("jvm")
}

version = "1.0"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":ksp-processor"))
    ksp(project(":ksp-processor"))
}

ksp {
    arg("package", "ru.tetraquark.ksp.test.sample.generated")
    arg("source", "ru.tetraquark.ksp.test.sample.jvm.ResourcesObj")
}
