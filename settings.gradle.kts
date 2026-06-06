rootProject.name = "Bloom"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")

// Группа shared
include(":shared:core:domain")
include(":shared:core:data")
include(":shared:core:navigation:api")
include(":shared:core:navigation:impl")

// Группа shared features
include(":shared:feature:auth:api")
include(":shared:feature:auth:impl")
include(":shared:feature:profile:api")
include(":shared:feature:profile:impl")
include(":shared:feature:makeup-bag:api")
include(":shared:feature:makeup-bag:impl")
include(":shared:feature:skin-diary:api")
include(":shared:feature:skin-diary:impl")
include(":shared:feature:routine:api")
include(":shared:feature:routine:impl")
include(":shared:core:ui")
