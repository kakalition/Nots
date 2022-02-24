package com.daggery.buildsrc

object ApplicationId {
    const val id = "com.daggery.nots"
}

object Modules {
    const val app = ":app"
    const val data = ":data"
    const val domain = ":domain"
}

object Release {
    const val versionCode = 1
    const val versionName = "1.0"
}

object Version {
    const val activityKtx = "1.4.0"
    const val androidJunit = "3.4.0"
    const val androidJunitKtx = "1.1.3"
    const val appCompat = "1.4.1"
    const val archCoreCommon = "2.1.0"
    const val archCoreRuntime = "2.1.0"
    const val archCoreTesting = "2.1.0"
    const val compileSdkVersion = 31
    const val constraintLayout = "2.1.3"
    const val coreKtx = "1.7.0"
    const val coroutineCore = "1.6.0"
    const val espressoCore = "1.1.3"
    const val hiltAndroid = "2.39"
    const val hiltAndroidCompilerKapt = "2.39"
    const val junit = "4.13.2"
    const val kotlinJson = "1.3.2"
    const val leakCanary = "2.8.1"
    const val lifecycleRuntimeKtx = "2.4.1"
    const val lifecycleViewModelKtx = "2.4.1"
    const val materialDesign = "1.5.0"
    const val minSdkVersion = 26
    const val mockitoCore = "4.2.0"
    const val navigationFragmentKtx = "2.4.1"
    const val navigationUi = "2.4.1"
    const val preferencesDataStore = "1.0.0"
    const val recyclerView = "1.2.1"
    const val robolectric = "4.6"
    const val roomCompilerKapt = "2.4.1"
    const val roomKtx = "2.4.1"
    const val roomRuntime = "2.4.1"
    const val splashScreen = "1.0.0-beta01"
    const val targetSdkVersion = 31
}

object Libraries {
    const val hiltAndroid = "com.google.dagger:hilt-android:${Version.hiltAndroid}"
    const val hiltAndroidCompilerKapt = "com.google.dagger:hilt-android-compiler:${Version.hiltAndroidCompilerKapt}"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Version.leakCanary}"
}

object KotlinLibraries {
    const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutineCore}"
    const val kotlinJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlinJson}"
}

object AndroidLibraries {
    const val activityKtx = "androidx.activity:activity-ktx:${Version.activityKtx}"
    const val appCompat = "androidx.appcompat:appcompat:${Version.appCompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
    const val coreKtx = "androidx.core:core-ktx:${Version.coreKtx}"
    const val preferencesDataStore = "androidx.datastore:datastore-preferences:${Version.preferencesDataStore}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycleRuntimeKtx}"
    const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycleViewModelKtx}"
    const val materialDesign = "com.google.android.material:material:${Version.materialDesign}"
    const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Version.navigationFragmentKtx}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Version.navigationUi}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Version.recyclerView}"
    const val roomCompilerKapt = "androidx.room:room-compiler:${Version.roomCompilerKapt}"
    const val roomKtx = "androidx.room:room-ktx:${Version.roomKtx}"
    const val roomRuntime = "androidx.room:room-runtime:${Version.roomRuntime}"
    const val splashScreen = "androidx.core:core-splashscreen:${Version.splashScreen}"
}

object TestLibraries {
    const val androidJunitKtx = "androidx.test.ext:junit-ktx:${Version.androidJunitKtx}"
    const val archCoreCommon = "androidx.arch.core:core-common:${Version.archCoreCommon}"
    const val archCoreRuntime = "androidx.arch.core:core-runtime:${Version.archCoreRuntime}"
    const val archCoreTesting = "androidx.arch.core:core-testing:${Version.archCoreTesting}"
    const val junit = "junit:junit:${Version.junit}"
    const val robolectric = "org.robolectric:robolectric:${Version.robolectric}"
}

object AndroidTestLibraries {
    const val androidJunit = "androidx.test.espresso:espresso-core:${Version.androidJunit}"
    const val espressoCore = "androidx.test.ext:junit:${Version.espressoCore}"
    const val mockitoCore = "org.mockito:mockito-core:${Version.mockitoCore}"
}