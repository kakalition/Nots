package com.daggery.nots

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.daggery.nots.databinding.ActivityMainBinding
import com.daggery.nots.home.view.HomeFragmentDirections
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.home.viewmodel.HomeViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(
            (this.application as NotsApplication).database
        )
    }
    private lateinit var binding: ActivityMainBinding

    private val fabOnClickListener = { view: View ->
        navController.navigate(HomeFragmentDirections.actionHomeFragmentToAddEditNoteFragment(
            uuid = "",
            isReading = false)
        )
    }

    lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        snackbar = Snackbar.make(
            binding.root,
            "TEST",
            1000
        )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.findNavController()
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id == R.id.homeFragment) {
                binding.fab.visibility = View.VISIBLE
            } else {
                binding.fab.visibility = View.GONE
            }
        }

        // Set toolbar
        setSupportActionBar(binding.appBar)
        setupActionBarWithNavController(navController)
        supportActionBar?.displayOptions = DISPLAY_SHOW_CUSTOM

        // OnClickListener
        binding.apply {
            fab.setOnClickListener(fabOnClickListener)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    fun showDeleteDialog() {
        MaterialAlertDialogBuilder(this)
            .setView(R.layout.dialog_delete)
            .setPositiveButton("Delete") { dialog, which ->
            }
            .setNegativeButton("Cancel") { dialog, which ->

            }
            .show()
    }
}
