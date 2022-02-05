package com.daggery.nots.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.daggery.nots.R
import com.daggery.nots.datastore.PreferencesKeys.THEME_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DATA_STORE_NAME = "nots_datastore"

private object PreferencesKeys {
    val THEME_KEY = intPreferencesKey("theme_key")
}

private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val appContext: Context) {

    private val dataStore = appContext.dataStore

    suspend fun changeThemePreference(themeRes: Int) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = themeRes
        }
    }

    val themePreference: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: R.style.DefaultDarkTheme
        }
}