package com.daggery.data.datastore

import androidx.datastore.preferences.core.intPreferencesKey

internal object PreferencesKeys {
    val THEME_KEY = intPreferencesKey("theme_key")
    val HOME_LAYOUT_KEY = intPreferencesKey("home_layout_key")
}