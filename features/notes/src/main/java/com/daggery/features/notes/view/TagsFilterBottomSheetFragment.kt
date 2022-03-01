package com.daggery.nots.home.view

import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.daggery.domain.entities.NoteTag
import com.daggery.features.notes.R
import com.daggery.features.notes.databinding.FragmentTagsFilterBottomSheetBinding
import com.daggery.nots.extractChecked
import com.daggery.nots.home.viewmodel.NotesViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TagsFilterBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "tagsFilterBottomSheetFragment"
    }

    private var _viewBinding: FragmentTagsFilterBottomSheetBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: NotesViewModel by activityViewModels()

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
                viewBinding.chipGroup.removeAllViews()
                viewModel.getAllTags().forEach(bindsChips)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        viewModel.updateTagByTagName((viewBinding.chipGroup.children.filterIsInstance<Chip>()).extractChecked())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private val bindsChips = { noteTag: NoteTag ->
        val chip = layoutInflater.inflate(R.layout.chip_filter, viewBinding.chipGroup, false) as Chip
        chip.text = noteTag.tagName
        chip.isChecked = noteTag.checked
        // TODO: Check this behavior
        // TODO: Ensure when checking, chipgroup layout doesn't change
        // chip.ensureAccessibleTouchTarget(48)
        viewBinding.chipGroup.addView(chip)
    }
}
