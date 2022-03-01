import com.daggery.buildsrc.Modules
import com.daggery.buildsrc.*

plugins {
    id("com.android.library")
    id("common-android")
}


dependencies {

    implementation(project(Modules.domain))

    // Room
    addRoom()

    // DataStore
    addPreferencesDataStore()

    // Kotlin Json
    addKotlinJson()
}

kapt {
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    correctErrorTypes = true
}