import com.daggery.buildsrc.*

plugins {
    id("com.android.library")
    id("common-android")
}

dependencies {

    // Module
    addDataDomain()
    implementation(project(Modules.sharedAssets))
    implementation(project(":features:tageditorsheet"))
    implementation(project(":features:bookeditorsheet"))

    // Android
    addAndroidMaterial()
    addFragmentKtx()
    addActivityKtx()
    addLifecycle()
}