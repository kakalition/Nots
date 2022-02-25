import com.daggery.buildsrc.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("common-android")
}

dependencies {

    // Module
    implementation(project(Modules.sharedAssets))
    implementation(project(Modules.data))
    implementation(project(Modules.domain))

    // Hilt
    addHilt()

    // Android
    addAndroidCore()
    addAndroidMaterial()
    addFragmentKtx()
    addNavigation()
    addLifecycle()

    // Test
    addJunit()

    // Android Test
    addAndroidTestJunit()
    addEspresso()
}
