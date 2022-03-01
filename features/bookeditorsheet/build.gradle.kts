import com.daggery.buildsrc.*

plugins {
    id("com.android.library")
    id("common-android")
}

dependencies {

    // Module
    implementation(project(Modules.sharedAssets))
    addDataDomain()

    // Android
    addAndroidMaterial()
    addFragmentKtx()
    addNavigation()
    addLifecycle()
}
