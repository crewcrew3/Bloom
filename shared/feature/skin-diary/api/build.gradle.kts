plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "ru.itis.bloom.shared.feature.skindiary.api"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.data)
            api(projects.shared.core.domain )
            api(projects.shared.core.navigation.api )
            //navigation
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.compose.nav3)

            api(libs.kotlinx.serialization.json)
            api(libs.kotlinx.datetime)
        }
    }
}