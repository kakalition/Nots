pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        kotlin("kapt") version "1.6.10"
        id("com.android.application") version "7.1.1" apply false
        id("com.android.library") version "7.1.1" apply false
        id("org.jetbrains.kotlin.android") version "1.6.10" apply false
        id("org.jetbrains.kotlin.jvm") version "1.6.10" apply false
        id("org.gradle.kotlin.kotlin-dsl") version "2.1.7" apply false
    }
}


rootProject.name = "Nots"
include(":app", ":data", ":domain")