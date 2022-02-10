package com.daggery.nots.settings.layout.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.daggery.nots.MainViewModel
import com.daggery.nots.databinding.FragmentHomeLayoutSettingsBinding
import com.daggery.nots.settings.layout.utils.HomeLayoutSettingsUtils

class HomeLayoutSettingsFragment : Fragment() {
    private var _viewBinding: FragmentHomeLayoutSettingsBinding? = null
    val viewBinding get() = _viewBinding!!

    val viewModel: MainViewModel by activityViewModels()

    private var _fragmentUtils: HomeLayoutSettingsUtils? = null
    val fragmentUtils get() = _fragmentUtils!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentHomeLayoutSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _fragmentUtils = HomeLayoutSettingsUtils(this)

        with(fragmentUtils) {
            bindsToolbar()
        }
    }
}