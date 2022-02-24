package com.daggery.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.daggery.data.R
import com.daggery.data.datastore.PreferencesKeys.HOME_LAYOUT_KEY
import com.daggery.data.datastore.PreferencesKeys.THEME_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DATA_STORE_NAME = "nots_datastore"

private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

@Singleton
internal class DataStoreManager @Inject constructor(@ApplicationContext private val appContext: Context) {

    private val dataStore = appContext.dataStore

    val themePreference: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: 0
        }

    suspend fun changeThemePreference(themeRes: Int) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = themeRes
        }
    }

    val homeLayoutPreference: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[HOME_LAYOUT_KEY] ?: 0
        }

    suspend fun changeHomeLayoutPreference(homeLayoutPref: Int) {
        dataStore.edit { preferences ->
            preferences[HOME_LAYOUT_KEY] = homeLayoutPref
        }
    }
}
