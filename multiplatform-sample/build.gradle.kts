plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

repositories {
    google()
    jcenter()
    mavenCentral()
}

kotlin {
    android()

    sourceSets {
        val commonMain by getting
        val androidMain by getting {
            dependencies {}
        }
    }
}

android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
