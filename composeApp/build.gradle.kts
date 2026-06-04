import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleService)
    alias(libs.plugins.firebase.crashlytics)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm()
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            //firebase
            implementation(libs.firebase.crashlytics)
            implementation(libs.firebase.analytics)
            implementation(project.dependencies.platform(libs.firebase.bom))
        }
        commonMain.dependencies {
            //модули
            implementation(projects.shared.core.navigation.impl)
            implementation(projects.shared.feature.auth.api)
            implementation(projects.shared.feature.auth.impl)
            implementation(projects.shared.core.ui)
            implementation(projects.shared.core.data)
            implementation(projects.shared.feature.makeupBag.api)
            implementation(projects.shared.feature.makeupBag.impl)
            implementation(projects.shared.feature.skinDiary.api)
            implementation(projects.shared.feature.skinDiary.impl)

            //DI
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            //navigation
            implementation(libs.compose.nav3)
            implementation(libs.viewmodel.nav3)

            //compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "ru.itis.bloom"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.itis.bloom"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "ru.itis.bloom.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ru.itis.bloom"
            packageVersion = "1.0.0"
        }
    }
}

//apply(plugin = "com.google.gms.google-services")
