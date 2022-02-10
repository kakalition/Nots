package com.daggery.nots.settings.layout.utils

import android.view.View
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.settings.layout.view.HomeLayoutSettingsFragment

class HomeLayoutSettingsUtils(private val fragment: HomeLayoutSettingsFragment) {

    private val navigationClickListener: (View) -> Unit = { _: View ->
        fragment.findNavController().navigateUp()
    }

    fun bindsToolbar() {
        fragment.viewBinding.toolbarBinding.apply {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener(navigationClickListener)
        }
    }
}