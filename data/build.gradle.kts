import com.daggery.buildsrc.Modules
import com.daggery.buildsrc.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("common-android")
}


dependencies {

    implementation(project(Modules.domain))

    // Coroutine
    addCoroutineCore()

    // Android
    addAndroidCore()

    // Hilt
    addHilt()

    // Room
    addRoom()

    // DataStore
    addPreferencesDataStore()

    // Kotlin Json
    addKotlinJson()

    addJunit()

    addAndroidTestJunit()
    addEspresso()
}

kapt {
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    correctErrorTypes = true
}