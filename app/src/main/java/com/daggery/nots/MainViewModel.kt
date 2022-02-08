package com.daggery.nots

import androidx.annotation.StyleRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor()
    : ViewModel() {

    @StyleRes private val _themeKey = MutableLiveData<Int>(0)
    @StyleRes val themeKey: LiveData<Int> = _themeKey
}