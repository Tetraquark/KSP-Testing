package ru.tetraquark.ksp.test.sample.jvm

import ru.tetraquark.ksp.test.processor.GenerateImpl
import ru.tetraquark.ksp.test.processor.GenerateRes

@GenerateImpl(generatedClassName = "FeatureStrings")
interface FeatureStringResources {
    @GenerateRes
    val successText: String
    @GenerateRes(externalName = "error_text")
    val errorText: String
}
