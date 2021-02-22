package ru.tetraquark.ksp.test.sample.jvm

// imports generated class by KSP
import ru.tetraquark.ksp.test.sample.generated.FeatureStrings

fun main() {
    val generatedClassObj: FeatureStringResources = FeatureStrings()

    println(generatedClassObj.successText)
    println(generatedClassObj.errorText)
}
