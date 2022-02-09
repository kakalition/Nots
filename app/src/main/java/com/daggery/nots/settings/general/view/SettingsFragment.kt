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
import com.daggery.nots.settings.general.viewmodel.SettingsFragmentUtils

class SettingsFragment : Fragment() {

    private var _viewBinding: FragmentSettingsBinding? = null
    internal val viewBinding get() = _viewBinding!!

    val viewModel: MainViewModel by activityViewModels()

    private lateinit var fragmentUtils: SettingsFragmentUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Instantiate Fragment Utils Class
        fragmentUtils = SettingsFragmentUtils(this)

        // Prepare Toolbar
        viewBinding.toolbarBinding.apply {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener(fragmentUtils.navigationClickListener)
        }

        with(fragmentUtils) {
            bindsLanguageSettings()
            bindsThemeSettings()
            bindsShowTimeSettings()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
