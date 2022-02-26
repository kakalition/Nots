package com.daggery.features.settings.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.daggery.sharedassets.R as SharedR
import com.daggery.features.settings.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _viewBinding: FragmentSettingsBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindsToolbar()
        bindsLanguageSettings()
        bindsThemeSettings()
        bindsHomeLayoutSettings()
        bindsShowTimeSettings()
        bindsShowLastEditedSettings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private val navigationClickListener: (View) -> Unit = { _: View ->
        findNavController().navigateUp()
    }

    private fun bindsToolbar() {
        viewBinding.toolbarBinding.apply {
            toolbar.setNavigationIcon(SharedR.drawable.ic_back)
            toolbar.setNavigationOnClickListener(navigationClickListener)
        }
    }

    private fun bindsLanguageSettings() {
        viewBinding.languageSettingsBinding.apply {
            settingsItemIcon.setImageResource(SharedR.drawable.ic_earth)
            settingsItemTitle.text = requireContext()
                .getString(SharedR.string.fragment_settings_language)
            settingsItemBody.text = "English"
        }
    }

    private fun bindsThemeSettings() {
        viewBinding.themeSettingsFrame.setOnClickListener {
/*
            findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToThemeSettingsFragment()
            )
*/
        }
        viewBinding.themeSettingsBinding.apply {
            settingsItemIcon.setImageResource(SharedR.drawable.ic_theme)
            settingsItemTitle.text = requireContext()
                .getString(SharedR.string.fragment_settings_theme)
            // settingsItemBody.text = viewModel.themeManager.getThemeName() + " Theme"
            settingsItemBody.text = "Active"
        }
    }

    private fun bindsHomeLayoutSettings() {
/*
        viewBinding.homeLayoutSettingsFrame.setOnClickListener {
            findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToHomeLayoutSettingsFragment()
            )
*/
    }

    private fun bindsShowTimeSettings() {
        viewBinding.showTimeBinding.apply {
            settingsItemIcon.setImageResource(SharedR.drawable.ic_clock)
/*
            settingsItemTitle.text = ContentProviderCompat.requireContext()
                .getString(SharedR.string.fragment_settings_show_time_text)
*/
            settingsItemBody.text = "Disabled"
        }
    }

    private fun bindsShowLastEditedSettings() {
        viewBinding.showLastEditedBinding.apply {
            settingsItemIcon.setImageResource(SharedR.drawable.ic_today_calendar)
/*
            settingsItemTitle.text = ContentProviderCompat.requireContext()
                .getString(SharedR.string.fragment_settings_show_last_edited_text)
*/
            settingsItemBody.text = "Disabled"
        }
    }
}

