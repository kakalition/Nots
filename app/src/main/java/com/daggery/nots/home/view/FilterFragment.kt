package com.daggery.nots.home.view

import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentFilterBinding
import com.daggery.nots.home.utils.FilterFragmentUtils
import com.daggery.nots.home.viewmodel.FilterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tagList.collect {
                    // Clear all children when list is updated
                    viewBinding.chipGroup.removeAllViews()
                    it.forEach { noteTag ->
                        val chip = layoutInflater.inflate(R.layout.chip_filter, viewBinding.chipGroup, false) as Chip
                        chip.text = noteTag.tagName
                        chip.isChecked = noteTag.checked
                        viewBinding.chipGroup.addView(chip)
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}