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
include(":shared:shared-core")
include(":shared:shared-domain")
include(":shared:shared-data")
include(":shared:shared-navigation:api")
include(":shared:shared-navigation:impl")

// Группа shared features
include(":shared:shared-feature:auth:api")
include(":shared:shared-feature:auth:impl")
include(":shared:shared-feature:profile:api")
include(":shared:shared-feature:profile:impl")
include(":shared:shared-feature:makeup-bag:api")
include(":shared:shared-feature:makeup-bag:impl")
include(":shared:shared-feature:skin-diary:api")
include(":shared:shared-feature:skin-diary:impl")
include(":shared:shared-feature:routine:api")
include(":shared:shared-feature:routine:impl")
