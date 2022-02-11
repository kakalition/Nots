package com.daggery.nots.settings.general.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.daggery.nots.MainViewModel
import com.daggery.nots.databinding.FragmentSettingsBinding
import com.daggery.nots.settings.general.utils.SettingsFragmentUtils

class SettingsFragment : Fragment() {

    private var _viewBinding: FragmentSettingsBinding? = null
    internal val viewBinding get() = _viewBinding!!

    val viewModel: MainViewModel by activityViewModels()

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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _fragmentUtils = null
    }
}
