package com.daggery.features.addviewnote.view

import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.daggery.nots.R
import com.daggery.features.addviewnote.viewmodel.AddViewNoteViewModel
import com.daggery.nots.databinding.FragmentAssignTagsBottomSheetBinding
import com.daggery.nots.extractChecked
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AssignTagsBottomSheetFragment(private val updateTagsCallback: (List<String>) -> Unit) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "assignTagsBottomSheetFragment"
    }

    private var _viewBinding: FragmentAssignTagsBottomSheetBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: AddViewNoteViewModel by activityViewModels()

    private var localTagNameList: List<String> = listOf()

    fun assignTagNameList(value: List<String>) {
        localTagNameList = value
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
            val tagNameList = viewModel.getTags().map { it.tagName }
            val tagNameIntersection = tagNameList.intersect(localTagNameList)

            tagNameList.forEach { tagName ->
                val chip = layoutInflater.inflate(R.layout.chip_filter, viewBinding.chipGroup, false) as Chip
                chip.apply {
                    chip.text = tagName
                    chip.isChecked = tagNameIntersection.contains(tagName)
                }
                // TODO: Check this behavior
                // TODO: Ensure when checking, chipgroup layout doesn't change
                // chip.ensureAccessibleTouchTarget(48)
                viewBinding.chipGroup.addView(chip)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        updateTagsCallback(viewBinding.chipGroup.children.filterIsInstance<Chip>().extractChecked())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
