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

    // Kotlin
    addCoroutineCore()

    // Hilt
    addHilt()

    // Android
    addAndroidCore()
    addAndroidMaterial()
    addFragmentKtx()
    addRecyclerView()
    addNavigation()

    // Lifecycle
    addLifecycle()

    // Test
    addJunit()

    // Android Test
    addAndroidTestJunit()
    addEspresso()
}