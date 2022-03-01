import com.daggery.buildsrc.*

plugins {
    id("com.android.library")
    id("common-android")
}

dependencies {

    // Module
    addDataDomain()
    implementation(project(Modules.sharedAssets))

    // Android
    addAndroidMaterial()
    addFragmentKtx()
    addActivityKtx()
    addLifecycle()
}