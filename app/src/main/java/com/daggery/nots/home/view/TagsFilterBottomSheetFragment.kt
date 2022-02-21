package com.daggery.nots.home.view

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
import com.daggery.nots.MainViewModel
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentTagsFilterBottomSheetBinding
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

    private val viewModel: MainViewModel by activityViewModels()

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
                viewModel.noteTagList.collect {
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val checkedTagsFromChipGroup = mutableListOf<String>()
        val idList = viewBinding.chipGroup.checkedChipIds
        for(i in idList) {
            checkedTagsFromChipGroup.add(view?.findViewById<Chip>(i)?.text.toString())
        }

        viewModel.updateTagByTagName(checkedTagsFromChipGroup)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}