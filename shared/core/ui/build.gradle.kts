plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "ru.itis.bloom.shared.core.ui"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }

    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            // Compose Multiplatform
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.components.resources)

            // Lifecycle ViewModel (для интеграции с MVI/ViewModel)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Navigation 3 (если компоненты используют навигацию)
            implementation(libs.compose.nav3)

            // Coil для изображений (если нужны иконки/аватары)
            implementation(libs.coil)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // Koin (опционально, если компоненты зависят от DI)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            // Coroutines & Collections
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)
        }
    }
}
