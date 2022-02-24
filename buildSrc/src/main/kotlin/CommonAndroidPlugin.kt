package com.daggery.buildsrc

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

class CommonAndroidPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("org.jetbrains.kotlin.android")
        project.pluginManager.apply("kotlin-kapt")

        val androidExtension = project.extensions.getByName("android")
        if(androidExtension is com.android.build.gradle.BaseExtension) {
            androidExtension.apply {
                compileSdkVersion(Version.compileSdkVersion)

                defaultConfig {
                    minSdk = Version.minSdkVersion
                    targetSdk = Version.targetSdkVersion

                    versionCode = Release.versionCode
                    versionName = Release.versionName

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                val proguardFile = "proguard-rules.pro"
                when(this) {
                    is com.android.build.gradle.LibraryExtension -> defaultConfig {
                        consumerProguardFiles(proguardFile)
                    }
                    is com.android.build.gradle.AppExtension -> buildTypes {
                        getByName("release") {
                            isMinifyEnabled = true
                            proguardFiles(
                                getDefaultProguardFile("proguard-android-optimize.txt"),
                                "proguard-rules.pro"
                            )
                        }
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }

                project.tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
                    kotlinOptions {
                        jvmTarget = "1.8"
                    }
                }
            }
        }
    }
}