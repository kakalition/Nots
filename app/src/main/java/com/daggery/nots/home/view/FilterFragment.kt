package com.daggery.nots.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentFilterBinding
import com.daggery.nots.home.utils.FilterFragmentUtils

class FilterFragment : Fragment() {

    private var _viewBinding: FragmentFilterBinding? = null
    internal val viewBinding get() = _viewBinding!!

    private var _fragmentUtils: FilterFragmentUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentFilterBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _fragmentUtils = FilterFragmentUtils(this)

        fragmentUtils.bindsToolbar()
    }
}