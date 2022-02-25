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

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")

    // Android
    addAndroidCore()
    addAndroidMaterial()
    addFragmentKtx()

    // Test
    addJunit()

    // Android Test
    addAndroidTestJunit()
    addEspresso()
}