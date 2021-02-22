buildscript {
    repositories {
        mavenCentral()
        jcenter()
        google()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.4.30"))
        classpath("com.android.tools.build:gradle:4.0.1")
    }
}
