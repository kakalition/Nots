import com.daggery.buildsrc.*

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("common-android")
}

android {
    defaultConfig {
        applicationId = ApplicationId.id
    }
}

dependencies {

    // Module
    implementation(project(Modules.sharedAssets))
    implementation(project(Modules.addViewNote))
    implementation(project(Modules.settings))
    implementation(project(Modules.dashboard))
    implementation(project(Modules.notes))
    implementation(project(Modules.tags))
    implementation(project(Modules.books))
    implementation(project(Modules.data))
    implementation(project(Modules.domain))

    // Kotlin Json
    addKotlinJson()

    // Kotlin Coroutine
    addCoroutineCore()

    // Android
    addAndroidCore()
    addSplashScreen()
    addAndroidMaterial()
    addRecyclerView()
    addActivityKtx()
    addFragmentKtx()

    // Room
    addRoom()

    // Navigation
    addNavigation()

    // Lifecycle
    addLifecycle()

    // Hilt
    addHilt()

    // Leak Canary
    addLeakCanary()

    // Test
    addJunit()
    addTestJunit()
    addArchTesting()
    addRobolectric()

    // Android Test
    addAndroidTestJunit()
    addEspresso()
}

kapt {
    correctErrorTypes = true
}