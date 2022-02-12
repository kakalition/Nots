package com.daggery.nots

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.daggery.nots.databinding.ActivityMainBinding
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint

// TODO: Upcoming Feature: Have bold italic underline feature

// TODO: Add Reorderable Feature TARGET
// TODO: Add Delete All TARGET
// TODO: Reorder to Chronological Order Feature
// TODO: Maybe surface color references should be changed to surface color variant to make surface looks brighter
// TODO: Preview Note Binder Can Be Unified
// TODO: Adjust outlined home layout color
// TODO: Create Notes Filter
// TODO: Fix SettingsFragment scrolling

// TODO: Known Bug: New note not placed at the top of list
// TODO: Fix by manually using sql commands
// TODO: Known Issue: MaterialYou text color is not clear

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _viewBinding: ActivityMainBinding? = null
    private val viewBinding get() = _viewBinding!!

    internal val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show SplashScreen until ThemeKey is Loaded
        installSplashScreen().setKeepOnScreenCondition { viewModel.themeKey == 0 }

        // Theme Setting
        setThemeOnInitialStart()
        getHomeLayoutOnInitialStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            viewModel.themeKey == R.style.MaterialYouTheme
        ) {
            DynamicColors.applyIfAvailable(this)
            setTheme(viewModel.themeKey)
        } else {
            setTheme(viewModel.themeKey)
        }
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
        if(viewModel.themeKey == -1){
            viewModel.themeDataStore.observeOnce(this) {
                viewModel.themeKey = it
                updateTheme(it)
            }
        }
    }

    private fun getHomeLayoutOnInitialStart() {
        if(viewModel.homeLayoutKey == -1) {
            viewModel.homeLayoutDataStore.observeOnce(this) {
                viewModel.homeLayoutKey = it
            }
        }
    }

    fun statusBarColorSetter() {
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