package com.daggery.nots

import android.app.Application
import com.daggery.nots.data.NotsDatabase
import com.google.android.material.color.DynamicColors

class NotsApplication : Application() {
    val database: NotsDatabase by lazy { NotsDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}