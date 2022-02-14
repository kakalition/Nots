package com.daggery.nots.settings.theme.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import com.daggery.nots.MainViewModel
import com.daggery.nots.databinding.FragmentThemeSettingsBinding
import com.daggery.nots.setMargin
import com.daggery.nots.settings.theme.utils.ThemeSettingsUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThemeSettingsFragment : Fragment() {

    private var _viewBinding: FragmentThemeSettingsBinding? = null
    val viewBinding get() = _viewBinding!!

    val viewModel: MainViewModel by activityViewModels()

    private var _fragmentUtils: ThemeSettingsUtil? = null
    val fragmentUtils get() = _fragmentUtils!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentThemeSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _fragmentUtils = ThemeSettingsUtil(this)

        with(fragmentUtils) {
            revertStatusBarColor()
            bindsToolbar()
            bindsCurrentTheme()
            bindsMaterialYouIfAvailable()
            bindsAvailableTheme()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _fragmentUtils = null
    }
}