package com.daggery.nots.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentFilterBinding
import com.daggery.nots.home.utils.FilterFragmentUtils
import com.daggery.nots.home.viewmodel.FilterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : Fragment() {

    private var _viewBinding: FragmentFilterBinding? = null
    internal val viewBinding get() = _viewBinding!!

    private val viewModel: FilterViewModel by activityViewModels()

    private var _fragmentUtils: FilterFragmentUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    val newTagsDialog = NewTagsDialogFragment()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}