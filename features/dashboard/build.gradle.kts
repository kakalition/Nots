import com.daggery.buildsrc.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("common-android")
}

dependencies {

    // Module
    implementation(project(Modules.sharedAssets))
    implementation(project(Modules.data))
    implementation(project(Modules.domain))

    addFragmentKtx()
    addActivityKtx()

    // Hilt
    addHilt()

    // Android
    addAndroidCore()
    addAndroidMaterial()
    //addLifecycle()

    // Test
    addJunit()

    // Android Test
    addAndroidTestJunit()
    addEspresso()
}