package com.daggery.nots.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.daggery.nots.MainViewModel
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    val viewModel: MainViewModel by activityViewModels()

    private lateinit var fragmentUtils: SettingsFragmentUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Instantiate Fragment Utils Class
        fragmentUtils = SettingsFragmentUtils(this)

        // Prepare Toolbar
        binding.toolbarBinding.apply {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener(fragmentUtils.navigationClickListener)
        }

        // Set Theme Settings Item Layout
        binding.themeSettingsFrame.setOnClickListener {
            findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToThemeSettingsFragment()
            )
        }
        binding.themeSettingsBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_theme)
            settingsItemTitle.text = "Theme"
            settingsItemBody.text = viewModel.getThemeName()
        }

        // Set DateFormat Settings Item Layout
        binding.dateFormatBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_calendar)
            settingsItemTitle.text = "Date Format"
            settingsItemBody.text = "Current: Sunday, 01 January 2022"
        }

        // Set ShowTime Settings Item Layout
        binding.showTimeBinding.apply {
            settingsItemIcon.setImageResource(R.drawable.ic_clock)
            settingsItemTitle.text = "Show Time"
            settingsItemBody.text = "Disabled"
        }
    }
}

private class SettingsFragmentUtils(private val fragment: SettingsFragment) {
    val navigationClickListener: (View) -> Unit = { view: View ->
        fragment.findNavController().navigateUp()
    }

}