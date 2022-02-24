import com.daggery.buildsrc.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("common-android")
}

dependencies {

    // Module
    implementation(project(Modules.data))
    implementation(project(Modules.domain))

    // Android
    addAndroidCore()
    addAndroidMaterial()

    // Test
    addJunit()

    // Android Test
    addAndroidTestJunit()
    addEspresso()
}