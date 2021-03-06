package com.daggery.nots

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.daggery.nots.databinding.ActivityMainBinding
import com.daggery.sharedassets.R as SharedR
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint

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

        // Initialization
        //statusBarColorSetter()
        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        viewBinding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_dashboard -> {
                    navController.navigate(SharedR.id.dashboardFragment)
                    true
                }
                R.id.nav_notes -> {
                    navController.navigate(SharedR.id.notesFragment)
                    true
                }
                R.id.nav_tags -> {
                    navController.navigate(SharedR.id.tagsFragment)
                    true
                }
                R.id.nav_books -> {
                    navController.navigate(SharedR.id.booksFragment)
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == SharedR.id.addViewNoteFragment) {
                viewBinding.bottomNavigation.visibility = View.GONE
            } else {
                viewBinding.bottomNavigation.visibility = View.VISIBLE
            }
        }

/*
        // Theme Setting
        setThemeOnInitialStart()
        getHomeLayoutOnInitialStart()

*/

/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            editorViewModel.themeManager.themeKey == R.style.MaterialYouTheme
        ) {
            DynamicColors.applyIfAvailable(this)
            setTheme(editorViewModel.themeManager.themeKey)
        } else {
            setTheme(editorViewModel.themeManager.themeKey)
        }
*/


    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

/*

    private fun setThemeOnInitialStart() {
        with(editorViewModel.themeManager) {
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
        with(editorViewModel.themeManager) {
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
            resources.getColor(SharedR.color.transparent, null)
        )
    }

    fun updateTheme(themeRes: Int) {
        //editorViewModel.applyTheme(themeRes)
        recreate()
    }
}