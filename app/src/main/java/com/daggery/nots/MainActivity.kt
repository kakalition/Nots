package com.daggery.nots

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.daggery.nots.databinding.ActivityMainBinding
import com.daggery.nots.home.view.HomeFragmentDirections
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.home.viewmodel.HomeViewModelFactory
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var viewBinding: ActivityMainBinding

    private val fabOnClickListener = { _ : View ->
        navController.navigate(HomeFragmentDirections.actionHomeFragmentToAddViewNoteFragment(
            uuid = "",
            isReading = false)
        )
        changeToolbarTitle("Add New Note")
    }

    fun changeToolbarTitle(title: String) {
        viewBinding.appBarTitle.text = title
    }

    fun resetToolbarTitle() {
        viewBinding.appBarTitle.text = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.findNavController()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.homeFragment) {
                viewBinding.fab.visibility = View.VISIBLE
            } else {
                viewBinding.fab.visibility = View.GONE
            }
        }

        // Set toolbar
        setSupportActionBar(viewBinding.appBar)
        setupActionBarWithNavController(navController)
        supportActionBar?.displayOptions = DISPLAY_SHOW_CUSTOM

        // OnClickListener
        viewBinding.apply {
            fab.setOnClickListener(fabOnClickListener)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}