package com.daggery.nots.utils.theme

import androidx.annotation.StyleRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.daggery.nots.R
import com.daggery.nots.datastore.DataStoreManager

class ThemeManager(private val dataStoreManager: DataStoreManager) {

    val themeDataStore: LiveData<Int> get() = dataStoreManager.themePreference.asLiveData()
    private var _themeKey: Int = -1
    val themeKey get() = _themeKey
    fun setThemeKey(@StyleRes newThemeKey: Int) {
        _themeKey = newThemeKey
    }

    val homeLayoutDataStore: LiveData<Int> get() = dataStoreManager.homeLayoutPreference.asLiveData()
    private var _homeLayoutKey: Int = -1
    val homeLayoutKey get() = _homeLayoutKey
    fun setHomeLayoutKey(@StyleRes newHomeLayoutKey: Int) {
        _homeLayoutKey = newHomeLayoutKey
    }

    fun getThemeName(): String {
        return when(themeKey) {
            R.style.MaterialYouTheme -> "Material You"
            R.style.DarkTheme -> "Dark"
            R.style.NordTheme -> "Nord"
            R.style.SteelBlueTheme -> "Steel Blue"
            R.style.RoyalLavenderTheme -> "Royal Lavender"
            R.style.HeatherBerryTheme -> "Heather Berry"
            else -> "Unspecified"
        }
    }

    fun getLayoutName(): String {
        return when(homeLayoutKey) {
            0 -> "Filled"
            1 -> "Outlined"
            else -> "Unspecified"
        }
    }
}
