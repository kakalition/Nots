import com.daggery.buildsrc.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("common-android")
}

dependencies {

    addAndroidCore()
    addAndroidMaterial()
    addSplashScreen()
    addJunit()
    addAndroidTestJunit()
    addEspresso()
}