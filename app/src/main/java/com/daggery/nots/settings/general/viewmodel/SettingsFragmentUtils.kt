package com.daggery.nots.settings.general.viewmodel

import android.view.View
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.home.view.SettingsFragment
import com.daggery.nots.home.view.SettingsFragmentDirections

class SettingsFragmentUtils(private val fragment: SettingsFragment) {
    val navigationClickListener: (View) -> Unit = { view: View ->
        fragment.findNavController().navigateUp()
    }

    fun bindsLanguageSettings() {
        fragment.viewBinding.languageSettingsBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_earth)
            settingsItemTitle.text = "Language"
            settingsItemBody.text = "English"
        }
    }

    fun bindsShowTimeSettings() {
        fragment.viewBinding.showTimeBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_clock)
            settingsItemTitle.text = "Show Time"
            settingsItemBody.text = "Disabled"
        }
    }

    fun bindsThemeSettings() {
        fragment.viewBinding.themeSettingsFrame.setOnClickListener {
            fragment.findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToThemeSettingsFragment()
            )
        }
        fragment.viewBinding.themeSettingsBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_theme)
            settingsItemTitle.text = "Theme"
            settingsItemBody.text = fragment.viewModel.getThemeName()
        }
    }

}
