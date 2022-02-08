package com.daggery.nots

import android.content.Context
import android.os.Bundle
import android.util.Log
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

    lateinit var viewBinding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show SplashScreen until ThemeKey is Loaded
        installSplashScreen().setKeepOnScreenCondition { viewModel.themeKey.value == 0 }
        viewModel.themeKey.observe(this) {
            Log.d("LOL", it.toString())
            setTheme(it)
        }

        window.statusBarColor = MaterialColors.getColor(
            this,
            com.google.android.material.R.attr.colorSurface,
            resources.getColor(R.color.transparent, null)
        )

        // Binder
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    fun updateTheme(themeRes: Int) {
        viewModel.applyTheme(themeRes)
        recreate()
    }
}