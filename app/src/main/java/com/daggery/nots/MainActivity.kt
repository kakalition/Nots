package com.daggery.nots

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import com.daggery.nots.databinding.ActivityMainBinding
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<HomeViewModel>()
    private val fabOnClickListener = { view: View ->
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.action_homeFragment_to_addEditNoteFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set toolbar
        setSupportActionBar(binding.appBar)

        // OnClickListener
        binding.fab.setOnClickListener(fabOnClickListener)

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
