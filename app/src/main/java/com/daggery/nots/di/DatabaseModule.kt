package com.daggery.nots.di

import android.content.Context
import com.daggery.nots.datastore.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object DatabaseModule {
    @Module
    @InstallIn(SingletonComponent::class)
    internal object DataStoreModule {
        @Provides
        @Singleton
        fun providesDataStore(@ApplicationContext appContext: Context): DataStoreManager {
            return DataStoreManager(appContext)
        }
    }
}