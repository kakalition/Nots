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

    // Android
    addAndroidMaterial()
    addFragmentKtx()
    addNavigation()
    addLifecycle()
}
