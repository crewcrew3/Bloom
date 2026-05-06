plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "ru.itis.bloom.shared.core"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

}

kotlin {
    androidTarget()

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
        }
    }
}