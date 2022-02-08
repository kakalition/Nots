package com.daggery.nots

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.daggery.nots.databinding.ActivityMainBinding
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.utils.GeneralUtils
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val generalUtils = GeneralUtils()
    lateinit var viewBinding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show SplashScreen until ThemeKey is Loaded
        installSplashScreen().setKeepOnScreenCondition { viewModel.themeKey == 0 }
        setTheme(viewModel.themeKey)
        generalUtils.prepareStatusBar(activity = this, themeKey = viewModel.themeKey)

        // Binder
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    fun updateTheme(themeRes: Int) {
        viewModel.applyTheme(themeRes)
        recreate()
    }
}