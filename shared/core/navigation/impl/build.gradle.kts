plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.kotlin.serialization)
}


kotlin {
    androidLibrary {
        namespace = "ru.itis.bloom.shared.core.navigation.impl"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    jvm()

    sourceSets {
        commonMain.dependencies {

            //modules
            implementation(projects.shared.core.navigation.api)
            implementation(projects.shared.feature.auth.api)
            implementation(projects.shared.feature.profile.api)
            implementation(projects.shared.feature.makeupBag.api)
            implementation(projects.shared.feature.routine.api)
            implementation(projects.shared.feature.skinDiary.api)

            //navigation
            implementation(libs.compose.nav3)
            implementation(libs.kotlinx.serialization.core)

            //DI
            implementation(libs.koin.core)
        }
    }
}