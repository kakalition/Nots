package com.daggery.nots.settings.general.viewmodel

import android.view.View
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.settings.general.view.SettingsFragment
import com.daggery.nots.settings.general.view.SettingsFragmentDirections

class SettingsFragmentUtils(private val fragment: SettingsFragment) {
    private val navigationClickListener: (View) -> Unit = { view: View ->
        fragment.findNavController().navigateUp()
    }

    fun bindsToolbar() {
        fragment.viewBinding.toolbarBinding.apply {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener(navigationClickListener)
        }
    }

    fun bindsLanguageSettings() {
        fragment.viewBinding.languageSettingsBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_earth)
            settingsItemTitle.text = fragment.requireContext()
                .getString(R.string.fragment_settings_language)
            settingsItemBody.text = "English"
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
            settingsItemTitle.text = fragment.requireContext()
                .getString(R.string.fragment_settings_theme)
            settingsItemBody.text = fragment.viewModel.getThemeName()
        }
    }

    fun bindsLayoutSettings() {
        fragment.viewBinding.layoutSettingsBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_earth)
            settingsItemTitle.text = fragment.requireContext()
                .getString(R.string.fragment_settings_note_layout)
            settingsItemBody.text = "Split"
        }
    }

    fun bindsShowTimeSettings() {
        fragment.viewBinding.showTimeBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_clock)
            settingsItemTitle.text = fragment.requireContext()
                .getString(R.string.fragment_settings_show_time)
            settingsItemBody.text = "Disabled"
        }
    }
}