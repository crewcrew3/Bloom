import java.io.FileInputStream
import java.util.Properties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.buildKonfig)
}

kotlin {
    android {
        namespace = "ru.itis.bloom.shared.core.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)

            implementation(libs.koin.core)
            //модули
            implementation(projects.shared.core.domain)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)

            api(libs.sqldelight.coroutines.extensions)
            api(libs.sqldelight.sqlite.adapter)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.logging)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.koin.android)
            implementation(libs.sqldelight.android.driver)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.logging)
            implementation(libs.kotlinx.coroutinesSwing)

            implementation(libs.sqldelight.sqlite.driver)
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("ru.itis.bloom.shared.core.data")
        }
    }
}

val apiConfigFile = rootProject.file("api_config.properties")
val apiConfig = Properties()
if (apiConfigFile.exists()) {
    apiConfig.load(FileInputStream(apiConfigFile))
} else {
    // Fallback значения, если файла нет (чтобы сборка не падала)
    apiConfig.setProperty("BLOOM_API_URL_PROD", "https://api.bloom-app.com/v1")
    apiConfig.setProperty("BLOOM_API_URL_DEV", "http://localhost:8080/v1")
    apiConfig.setProperty("BLOOM_IS_DEBUG", "true")
}

buildkonfig {
    packageName = "ru.itis.bloom.shared.core.data"

    defaultConfigs {
        buildConfigField(STRING, "API_BASE_URL", apiConfig.getProperty("BLOOM_API_URL_PROD"))
        buildConfigField(BOOLEAN, "IS_DEBUG", "false")
    }
    targetConfigs {
        create("debug") {
            buildConfigField(STRING, "API_BASE_URL", apiConfig.getProperty("BLOOM_API_URL_DEV"))
            buildConfigField(BOOLEAN, "IS_DEBUG", apiConfig.getProperty("BLOOM_IS_DEBUG", "true"))
        }

        create("release") {
            buildConfigField(STRING, "API_BASE_URL", apiConfig.getProperty("BLOOM_API_URL_PROD"))
            buildConfigField(BOOLEAN, "IS_DEBUG", "false")
        }
    }
}