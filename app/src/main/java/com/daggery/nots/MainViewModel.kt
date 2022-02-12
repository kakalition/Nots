package com.daggery.nots

import androidx.annotation.StyleRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.daggery.nots.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LAYOUT_FILLED = 0
private const val LAYOUT_OUTLINED = 1

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStoreManager
) : ViewModel() {

    val themeDataStore: LiveData<Int> get() = dataStore.themePreference.asLiveData()
    var themeKey: Int = -1

    val homeLayoutDataStore: LiveData<Int> get() = dataStore.homeLayoutPreference.asLiveData()
    var homeLayoutKey: Int = -1

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

    fun applyTheme(@StyleRes themeRes: Int) {
        viewModelScope.launch {
            dataStore.changeThemePreference(themeRes)
        }
        themeKey = themeRes
    }

    fun applyLayout(layoutId: Int) {
        viewModelScope.launch {
            dataStore.changeHomeLayoutPreference(layoutId)
        }
        homeLayoutKey = layoutId
    }

}