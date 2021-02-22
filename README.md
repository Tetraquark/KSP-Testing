# KSP-Testing

Playground project for testing [Kotlin Symbol Processing (KSP)](https://github.com/google/ksp) library by Google. 

It generates a class that implements interfaces annotated by `GenerateImpl`.

Result example:

```kotlin
package ru.tetraquark.ksp.test.sample.generated

import kotlin.String
import ru.tetraquark.ksp.test.sample.jvm.FeatureStringResources

public class FeatureStrings : FeatureStringResources {
  public override val successText: String =
      ru.tetraquark.ksp.test.sample.jvm.ResourcesObj.successText

  public override val errorText: String = ru.tetraquark.ksp.test.sample.jvm.ResourcesObj.error_text
}
```
