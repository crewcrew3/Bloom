plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidLibrary {
        namespace = "ru.itis.bloom.shared.feature.skindiary.impl"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            //modules
            implementation(projects.shared.feature.skinDiary.api)
            implementation(projects.shared.core.data)
            implementation(projects.shared.core.ui)
            implementation(projects.shared.core.navigation.api)

            //navigation
            implementation(libs.compose.nav3)

            //DI
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.vm)

            //compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.toast)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            //network
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.datetime)

            implementation(libs.sqldelight.coroutines.extensions)

            implementation(libs.coil)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)


            implementation(libs.kotlinx.immutable)
        }
    }
}