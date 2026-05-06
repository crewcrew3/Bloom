plugins {
    alias(libs.plugins.kotlinMultiplatform)
    //alias(libs.plugins.kotlin.android)
}

kotlin {
    //androidTarget()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
        }
    }
}