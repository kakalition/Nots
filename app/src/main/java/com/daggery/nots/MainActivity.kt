package com.daggery.nots

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.daggery.nots.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint

// TODO: Clear Focus on Back Pressed

// TODO: Migrate to Kotlin Flow

// TODO: Upcoming Feature: Have bold italic underline feature
// TODO: Upcoming Feature: Create different note layout. Maybe one page have 4 grid
// tODO: Upcoming Feature: Tags
// TODO: Upcoming Feature: Create Notes Filter

// TODO: Known Issue: MaterialYou text color is not clear
// TODO: Known Bug: Strange behaviour when reordering from priority to non-priority

// TODO: Maybe surface color references should be changed to surface color variant to make surface looks brighter
// TODO: Adjust outlined home layout color

// TODO: Preview Note Binder Can Be Unified

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _viewBinding: ActivityMainBinding? = null
    private val viewBinding get() = _viewBinding!!

    internal val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show SplashScreen until ThemeKey is Loaded
        installSplashScreen().setKeepOnScreenCondition { viewModel.themeManager.themeKey == 0 }

        // Theme Setting
        setThemeOnInitialStart()
        getHomeLayoutOnInitialStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            viewModel.themeManager.themeKey == R.style.MaterialYouTheme
        ) {
            DynamicColors.applyIfAvailable(this)
            setTheme(viewModel.themeManager.themeKey)
        } else {
            setTheme(viewModel.themeManager.themeKey)
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
        with(viewModel.themeManager) {
            if(themeKey == -1){
                themeDataStore.observeOnce(this@MainActivity) {
                    setThemeKey(it)
                    updateTheme(it)
                }
            }
        }
    }

    private fun getHomeLayoutOnInitialStart() {
        with(viewModel.themeManager) {
            if(homeLayoutKey == -1) {
                homeLayoutDataStore.observeOnce(this@MainActivity) {
                    setHomeLayoutKey(it)
                }
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