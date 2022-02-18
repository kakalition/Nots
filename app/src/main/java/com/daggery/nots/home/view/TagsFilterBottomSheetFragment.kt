package com.daggery.nots.home.view

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentTagsFilterBottomSheetBinding
import com.daggery.nots.home.viewmodel.FilterViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TagsFilterBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "tagsFilterBottomSheetFragment"
    }

    private var _viewBinding: FragmentTagsFilterBottomSheetBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: FilterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentTagsFilterBottomSheetBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tagList.collect {
                    // Clear all children when list is updated
                    viewBinding.chipGroup.removeAllViews()
                    it.forEach { noteTag ->
                        val chip = layoutInflater.inflate(R.layout.chip_filter, viewBinding.chipGroup, false) as Chip
                        chip.text = noteTag.tagName
                        chip.isChecked = noteTag.checked
                        // TODO: Check this behavior
                        // TODO: Ensure when checking, chipgroup layout doesn't change
                        // chip.ensureAccessibleTouchTarget(48)
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
/*
viewModel.tagList.collect {
}
*/
