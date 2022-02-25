package com.daggery.nots.settings.general.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentSettingsBinding
import com.daggery.nots.settings.general.utils.SettingsFragmentUtils

class SettingsFragment : Fragment() {

    private var _viewBinding: FragmentSettingsBinding? = null
    internal val viewBinding get() = _viewBinding!!

    //val viewModel: MainViewModel by activityViewModels()

    private var _fragmentUtils: SettingsFragmentUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _fragmentUtils = SettingsFragmentUtils(this)

        with(fragmentUtils) {
            bindsToolbar()
            bindsLanguageSettings()
            bindsThemeSettings()
            bindsHomeLayoutSettings()
            bindsShowTimeSettings()
            bindsShowLastEditedSettings()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _fragmentUtils = null
    }

    private val navigationClickListener: (View) -> Unit = { _: View ->
        findNavController().navigateUp()
    }

    private fun bindsToolbar() {
        viewBinding.toolbarBinding.apply {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener(navigationClickListener)
        }
    }

    private fun bindsLanguageSettings() {
        viewBinding.languageSettingsBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_earth)
            settingsItemTitle.text = requireContext()
                .getString(R.string.fragment_settings_language)
            settingsItemBody.text = "English"
        }
    }

    private fun bindsThemeSettings() {
        viewBinding.themeSettingsFrame.setOnClickListener {
            findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToThemeSettingsFragment()
            )
        }
        viewBinding.themeSettingsBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_theme)
            settingsItemTitle.text = requireContext()
                .getString(R.string.fragment_settings_theme)
            // settingsItemBody.text = viewModel.themeManager.getThemeName() + " Theme"
            settingsItemBody.text = "Active"
        }
    }

    private fun bindsHomeLayoutSettings() {
        viewBinding.homeLayoutSettingsFrame.setOnClickListener {
            findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToHomeLayoutSettingsFragment()
            )
        }
        viewBinding.homeLayoutSettingsBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_list_alt)
            settingsItemTitle.text = requireContext()
                .getString(R.string.fragment_settings_home_layout)
            // settingsItemBody.text = viewModel.themeManager.getLayoutName()
            settingsItemBody.text = "Active"
        }
    }

    private fun bindsShowTimeSettings() {
        viewBinding.showTimeBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_clock)
            settingsItemTitle.text = requireContext()
                .getString(R.string.fragment_settings_show_time_text)
            settingsItemBody.text = "Disabled"
        }
    }

    private fun bindsShowLastEditedSettings() {
        viewBinding.showLastEditedBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_today_calendar)
            settingsItemTitle.text = requireContext()
                .getString(R.string.fragment_settings_show_last_edited_text)
            settingsItemBody.text = "Disabled"
        }
    }
}
