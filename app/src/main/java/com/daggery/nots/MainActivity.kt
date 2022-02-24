package com.daggery.nots

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.daggery.nots.databinding.ActivityMainBinding
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint

// TODO: Refactor with clean architecture
// TODO: create repository
// TODO: implement correct data flow

// TODO: Modify bookmarked note layout as in Figma

// TODO: Known Issue: MaterialYou text color is not clear
// TODO: Known Issue: Tags Sorting in ManageTagsFragment
// TODO: Known Bug: Keyboard automatically closing when typing in AddViewNoteFragment (Emulator)
// TODO: Known Bug: Strange behaviour when reordering from priority to non-priority
// TODO: Known Bug: Theme not loaded on time

// TODO: Maybe surface color references should be changed to surface color variant to make surface looks brighter
// TODO: Adjust outlined home layout color
// TODO: Preview Note Binder Can Be Unified
// TODO: Simplify NoteListAdapter

// TODO: Books that located in drawer
// TODO: Upcoming Feature: Create different note layout. Maybe one page have 4 grid


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _viewBinding: ActivityMainBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show SplashScreen until ThemeKey is Loaded
        installSplashScreen()

/*
        // Theme Setting
        setThemeOnInitialStart()
        getHomeLayoutOnInitialStart()

*/

/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            viewModel.themeManager.themeKey == R.style.MaterialYouTheme
        ) {
            DynamicColors.applyIfAvailable(this)
            setTheme(viewModel.themeManager.themeKey)
        } else {
            setTheme(viewModel.themeManager.themeKey)
        }
*/

        statusBarColorSetter()

        // Binder
        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

/*

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

*/
/*

    private fun getHomeLayoutOnInitialStart() {
        with(viewModel.themeManager) {
            if(homeLayoutKey == -1) {
                homeLayoutDataStore.observeOnce(this@MainActivity) {
                    setHomeLayoutKey(it)
                }
            }
        }
    }
*/

    fun statusBarColorSetter() {
        window.statusBarColor = MaterialColors.getColor(
            this,
            com.google.android.material.R.attr.colorSurface,
            resources.getColor(R.color.transparent, null)
        )
    }

    fun updateTheme(themeRes: Int) {
        //viewModel.applyTheme(themeRes)
        recreate()
    }
}