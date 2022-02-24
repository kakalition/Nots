package com.daggery.buildsrc

object ApplicationId {
    val id = "com.daggery.nots"
}

object Modules {
    val app = ":app"
    val data = ":data"
    val domain = ":domain"
}

object Release {
    val versionCode = 1
    val versionName = "1.0"
}

object Version {
    val activityKtx = "1.4.0"
    val androidJunit = "3.4.0"
    val androidJunitKtx = "1.1.3"
    val appCompat = "1.4.1"
    val archCoreCommon = "2.1.0"
    val archCoreRuntime = "2.1.0"
    val archCoreTesting = "2.1.0"
    val constraintLayout = "2.1.3"
    val coreKtx = "1.7.0"
    val coroutineCore = "1.6.0"
    val espressoCore = "1.1.3"
    val hiltAndroid = "2.39"
    val hiltAndroidCompilerKapt = "2.39"
    val junit = "4.13.2"
    val kotlinJson = "1.3.2"
    val leakCanary = "2.8.1"
    val lifecycleRuntimeKtx = "2.4.1"
    val lifecycleViewModelKtx = "2.4.1"
    val materialDesign = "1.5.0"
    val mockitoCore = "4.2.0"
    val navigationFragmentKtx = "2.4.1"
    val navigationUi = "2.4.1"
    val recyclerView = "1.2.1"
    val robolectric = "4.6"
    val roomCompilerAnnotationProcessor = "2.4.1"
    val roomCompilerKapt = "2.4.1"
    val roomKtx = "2.4.1"
    val roomRuntime = "2.4.1"
    val splashScreen = "1.0.0-beta01"
}

object Libraries {
    val hiltAndroid = "com.google.dagger:hilt-android:${Version.hiltAndroid}"
    val hiltAndroidCompilerKapt = "com.google.dagger:hilt-android-compiler:${Version.hiltAndroidCompilerKapt}"
    val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Version.leakCanary}"
}

object KotlinLibraries {
    val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutineCore}"
    val kotlinJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlinJson}"
}

object AndroidLibraries {
    val activityKtx = "androidx.activity:activity-ktx:${Version.activityKtx}"
    val appCompat = "androidx.appcompat:appcompat:${Version.appCompat}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
    val coreKtx = "androidx.core:core-ktx:${Version.coreKtx}"
    val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycleRuntimeKtx}"
    val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycleViewModelKtx}"
    val materialDesign = "com.google.android.material:material:${Version.materialDesign}"
    val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Version.navigationFragmentKtx}"
    val navigationUi = "androidx.navigation:navigation-ui-ktx:${Version.navigationUi}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Version.recyclerView}"
    val roomCompilerAnnotationProcessor = "androidx.room:room-compiler:${Version.roomCompilerAnnotationProcessor}"
    val roomCompilerKapt = "androidx.room:room-compiler:${Version.roomCompilerKapt}"
    val roomKtx = "androidx.room:room-ktx:${Version.roomKtx}"
    val roomRuntime = "androidx.room:room-runtime:${Version.roomRuntime}"
    val splashScreen = "androidx.core:core-splashscreen:${Version.splashScreen}"
}

object TestLibraries {
    val androidJunitKtx = "androidx.test.ext:junit-ktx:${Version.androidJunitKtx}"
    val archCoreCommon = "androidx.arch.core:core-common:${Version.archCoreCommon}"
    val archCoreRuntime = "androidx.arch.core:core-runtime:${Version.archCoreRuntime}"
    val archCoreTesting = "androidx.arch.core:core-testing:${Version.archCoreTesting}"
    val junit = "junit:junit:${Version.junit}"
    val robolectric = "org.robolectric:robolectric:${Version.robolectric}"
}

object AndroidTestLibraries {
    val androidJunit = "androidx.test.espresso:espresso-core:${Version.androidJunit}"
    val espressoCore = "androidx.test.ext:junit:${Version.espressoCore}"
    val mockitoCore = "org.mockito:mockito-core:${Version.mockitoCore}"
}