package com.daggery.buildsrc

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.addDataDomain() {
    dependencies {
        add("implementation", project(":data"))
        add("implementation", project(":domain"))
    }
}

fun Project.addKotlinJson() {
    dependencies {
        add("implementation", KotlinLibraries.kotlinJson)
    }
}

fun Project.addCoroutineCore() {
    dependencies {
        add("implementation", KotlinLibraries.coroutineCore)
    }
}

fun Project.addAndroidMaterial() {
    dependencies {
        add("implementation", AndroidLibraries.materialDesign)
    }
}

fun Project.addRecyclerView() {
    dependencies {
        add("implementation", AndroidLibraries.recyclerView)
    }
}

fun Project.addSplashScreen() {
    dependencies {
        add("implementation", AndroidLibraries.splashScreen)
    }
}

fun Project.addAndroidCore() {
    dependencies {
        add("implementation", AndroidLibraries.appCompat)
        add("implementation", AndroidLibraries.coreKtx)
    }
}

fun Project.addRoom() {
    dependencies {
        add("implementation", AndroidLibraries.roomRuntime)
        add("implementation", AndroidLibraries.roomKtx)
        add("kapt", AndroidLibraries.roomCompilerKapt)
    }
}

fun Project.addPreferencesDataStore() {
    dependencies {
        add("implementation", AndroidLibraries.preferencesDataStore)
    }
}

fun Project.addNavigation() {
    dependencies {
        add("implementation", AndroidLibraries.navigationFragmentKtx)
        add("implementation", AndroidLibraries.navigationUi)
    }
}

fun Project.addActivityKtx() {
    dependencies {
        add("implementation", AndroidLibraries.activityKtx)
    }
}

fun Project.addFragmentKtx() {
    dependencies {
        add("implementation", AndroidLibraries.fragmentKtx)
    }
}

fun Project.addLifecycle() {
    dependencies {
        add("implementation", AndroidLibraries.lifecycleRuntimeKtx)
        add("implementation", AndroidLibraries.lifecycleViewModelKtx)
    }
}

fun Project.addHilt() {
    dependencies {
        add("implementation", Libraries.hiltAndroid)
        add("kapt", Libraries.hiltAndroidCompilerKapt)
    }
}

fun Project.addLeakCanary() {
    dependencies {
        add("debugImplementation", Libraries.leakCanary)
    }
}

fun Project.addArchTesting() {
    dependencies {
        add("testImplementation", TestLibraries.archCoreCommon)
        add("testImplementation", TestLibraries.archCoreRuntime)
        add("testImplementation", TestLibraries.archCoreTesting)
    }
}

fun Project.addTestJunit() {
    dependencies {
        add("testImplementation", TestLibraries.androidJunitKtx)
    }
}

fun Project.addJunit() {
    dependencies {
        add("testImplementation", TestLibraries.junit)
    }
}

fun Project.addRobolectric() {
    dependencies {
        add("testImplementation", TestLibraries.robolectric)
    }
}

fun Project.addEspresso() {
    dependencies {
        add("testImplementation", AndroidTestLibraries.espressoCore)
    }
}

fun Project.addAndroidTestJunit() {
    dependencies {
        add("testImplementation", AndroidTestLibraries.androidJunit)
    }
}
