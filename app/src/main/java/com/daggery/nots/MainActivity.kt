package com.daggery.nots

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.daggery.nots.databinding.ActivityMainBinding
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var viewBinding: ActivityMainBinding

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition { viewModel.themeKey == 0 }

        setTheme(viewModel.themeKey)
        prepareStatusBar()

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    fun updateTheme(themeRes: Int) {
        viewModel.applyTheme(themeRes)
        recreate()
    }

    fun prepareStatusBar() {
        window.apply {
            // Set Status Bar Color
            statusBarColor = MaterialColors.getColor(
                this@MainActivity,
                com.google.android.material.R.attr.colorSurface,
                resources.getColor(R.color.transparent, null)
            )

            // Set Status Bar Icon Color
            when(viewModel.themeKey) {
                R.style.DefaultDarkTheme -> decorView.systemUiVisibility = 0
                R.style.AzaleaTheme -> decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}