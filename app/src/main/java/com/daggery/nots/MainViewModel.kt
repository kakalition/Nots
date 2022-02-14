package com.daggery.nots

import androidx.annotation.StyleRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggery.nots.datastore.DataStoreManager
import com.daggery.nots.utils.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    val themeManager: ThemeManager,
) : ViewModel() {

    fun applyTheme(@StyleRes themeRes: Int) {
        viewModelScope.launch {
            dataStoreManager.changeThemePreference(themeRes)
        }
        themeManager.setThemeKey(themeRes)
    }

    fun applyLayout(layoutId: Int) {
        viewModelScope.launch {
            dataStoreManager.changeHomeLayoutPreference(layoutId)
        }
        themeManager.setHomeLayoutKey(layoutId)
    }
}