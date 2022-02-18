package com.daggery.nots.addviewnote.view

import android.content.DialogInterface
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
import com.daggery.nots.databinding.FragmentAssignTagsBottomSheetBinding
import com.daggery.nots.databinding.FragmentTagsFilterBottomSheetBinding
import com.daggery.nots.home.viewmodel.FilterViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AssignTagsBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "assignTagsBottomSheetFragment"
    }
    private var _viewBinding: FragmentAssignTagsBottomSheetBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: FilterViewModel by activityViewModels()

    private var tagNameList: List<String> = listOf()

    fun assignTagNameList(value: List<String>) {
        tagNameList = value
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentAssignTagsBottomSheetBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // TODO: Maybe can use DiffUtil
                viewModel.tagList.collect {
                    val tagListTagName = it.map { noteTag -> noteTag.tagName }
                    val tagNameIntersection: List<String> = tagNameList.intersect(tagListTagName).toList()

                    it.forEach { noteTag ->
                        val chip = layoutInflater.inflate(R.layout.chip_filter, viewBinding.chipGroup, false) as Chip
                        chip.text = noteTag.tagName
                        chip.isChecked = tagNameIntersection.contains(noteTag.tagName)
                        // TODO: Check this behavior
                        // TODO: Ensure when checking, chipgroup layout doesn't change
                        // chip.ensureAccessibleTouchTarget(48)
                        viewBinding.chipGroup.addView(chip)
                    }
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val idList = viewBinding.chipGroup.checkedChipIds

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
