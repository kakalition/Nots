import com.daggery.buildsrc.*

plugins {
    id("com.android.application")
    id("common-android")
}

android {
    defaultConfig {
        applicationId = ApplicationId.id
    }
}

dependencies {

    // Module
    addDataDomain()
    implementation(project(Modules.sharedAssets))
    implementation(project(Modules.addViewNote))
    implementation(project(Modules.settings))
    implementation(project(Modules.dashboard))
    implementation(project(Modules.notes))
    implementation(project(Modules.tags))
    implementation(project(Modules.books))

    // Kotlin Json
    addKotlinJson()

    // Android
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

    // Leak Canary
    addLeakCanary()

    // Test
    addTestJunit()
    addArchTesting()
    addRobolectric()
}

kapt {
    correctErrorTypes = true
}