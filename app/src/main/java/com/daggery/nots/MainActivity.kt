package com.daggery.nots

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.daggery.nots.databinding.ActivityMainBinding
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _viewBinding: ActivityMainBinding? = null
    private val viewBinding = _viewBinding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show SplashScreen until ThemeKey is Loaded
        installSplashScreen().setKeepOnScreenCondition { viewModel.themeKey == 0 }

        // Theme Setting
        setThemeOnInitialStart()
        setTheme(viewModel.themeKey)
        statusBarColorSetter()

        // Binder
        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    private fun setThemeOnInitialStart() {
        if(viewModel.themeKey == 0){
            viewModel.themeDataStore.observeOnce(this) {
                viewModel.themeKey = it
                updateTheme(it)
            }
        }
    }

    private fun statusBarColorSetter() {
        window.statusBarColor = MaterialColors.getColor(
            this,
            com.google.android.material.R.attr.colorSurface,
            resources.getColor(R.color.transparent, null)
        )
    }

    fun updateTheme(themeRes: Int) {
        viewModel.applyTheme(themeRes)
        recreate()
    }
}