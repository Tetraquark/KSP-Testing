package ru.tetraquark.ksp.test.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*

class KspGenProcessor : SymbolProcessor {
    private lateinit var codeGenerator: CodeGenerator
    private lateinit var logger: KSPLogger
    private lateinit var generatePackage: String
    private lateinit var propertyValueSource: String

    override fun finish() = Unit

    override fun init(
        options: Map<String, String>,
        kotlinVersion: KotlinVersion,
        codeGenerator: CodeGenerator,
        logger: KSPLogger
    ) {
        this.codeGenerator = codeGenerator
        this.logger = logger
        generatePackage = options["package"]!!
        propertyValueSource = options["source"]!!
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("ru.tetraquark.ksp.test.processor.GenerateImpl")
        val ret = symbols.filter { !it.validate() }
        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .map { it.accept(FindFunctionsVisitor(), Unit) }
        return ret
    }

    inner class FindFunctionsVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            if (classDeclaration.classKind != ClassKind.INTERFACE) return

            val packageName = classDeclaration.packageName.asString()
            val simpleClassName = classDeclaration.simpleName.asString()
            val implClassName = createClassImplName(classDeclaration)

            val classBuilder = TypeSpec.classBuilder(implClassName)
                .addSuperinterface(ClassName(packageName, simpleClassName))

            buildInterfaceImplementation(
                classDeclaration = classDeclaration,
                builder = classBuilder
            )

            // Create new file for generated code
            val file = codeGenerator.createNewFile(
                dependencies = Dependencies(true, classDeclaration.containingFile!!),
                packageName = generatePackage,
                fileName = implClassName,
            )

            // Generate file and save
            val fileSpec = FileSpec.builder(packageName = generatePackage, fileName = implClassName)
                .addType(classBuilder.build())
                .build()
            file.write(fileSpec.toString().toByteArray())
            file.close()
        }

        private fun createClassImplName(
            classDeclaration: KSClassDeclaration,
            defaultName: String = classDeclaration.simpleName.asString() + "Impl"
        ): String {
            return classDeclaration.annotations
                .find { it.shortName.asString() == "GenerateImpl" }
                ?.arguments
                ?.find { it.name?.asString() == "generatedClassName" }
                ?.let { if (it.value != "") it.value.toString() else null }
                ?: defaultName
        }

        private fun buildInterfaceImplementation(
            classDeclaration: KSClassDeclaration,
            builder: TypeSpec.Builder
        ): TypeSpec.Builder {
            classDeclaration.getAllProperties()
                .asSequence()
                .map { property ->
                    val annotation = property.annotations.find { it.shortName.asString() == "GenerateRes" }
                    if (annotation != null) {
                        property to annotation
                    } else {
                        null
                    }
                }
                .filterNotNull()
                .forEach { pair ->
                    val propertyName = pair.second.arguments
                        .find { it.name?.asString() == "externalName" }
                        ?.value
                        ?.let { if (it != "") it else null }
                        ?: pair.first.simpleName.asString()

                    val kProperty = PropertySpec.builder(
                        name = pair.first.simpleName.asString(),
                        type = pair.first.type.resolve().declaration.run {
                            val classname = qualifiedName?.asString() ?: simpleName.asString()
                            ClassName.bestGuess(classname)
                        }
                    ).addModifiers(KModifier.OVERRIDE).initializer("$propertyValueSource.$propertyName").build()

                    builder.addProperty(kProperty)
                }
            return builder
        }
    }
}
