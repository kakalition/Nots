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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Module
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