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

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStoreManager
) : ViewModel() {

    val themeDataStore: LiveData<Int> = dataStore.themePreference.asLiveData()
    var themeKey: Int = 0

    fun applyTheme(@StyleRes themeRes: Int) {
        viewModelScope.launch {
            dataStore.changeThemePreference(themeRes)
        }
        themeKey = themeRes
    }
}