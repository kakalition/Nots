package com.daggery.features.tageditorsheet.view

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.daggery.features.tageditorsheet.databinding.FragmentTagEditorSheetBinding
import com.daggery.features.tageditorsheet.viewmodel.TagEditorSheetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Test: Is AddTagUseCase works as expected

class TagEditorSheetFragment @Inject constructor(): BottomSheetDialogFragment() {

    companion object {
        const val TAG = "NewTagsDialog"
    }

    private var _viewBinding: FragmentTagEditorSheetBinding? = null
    val viewBinding get() = _viewBinding!!

    private val viewModel: TagEditorSheetViewModel by activityViewModels()

    private fun showKeyboard(view: View) {
        val inputMethodManager = requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentTagEditorSheetBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.confirmButton.setOnClickListener {

            val tag = viewBinding.newTagInput.text.toString()
/*
            when {
                tag.isBlank() -> {
                    Snackbar.make(viewBinding.root, "Tag is empty", Snackbar.LENGTH_SHORT)
                        .setAnchorView(viewBinding.newTagInput)
                        .show()
                }

                tag.isNotBlank() && !viewModel.isEditingTag() -> {
                    viewModel.addTag(NoteTag(id = 0, tagName = tag, checked = false))
                    this.dismiss()
                }

                tag.isNotBlank() && viewModel.isEditingTag() -> {
                    viewModel.getEditTag().let {
                        viewModel.updateTag(it.copy(tagName = tag))
                        this.dismiss()
                    }
                }
            }
*/
        }
    }

    override fun onStart() {
        super.onStart()
/*
        if(viewModel.checkedTagList.value.isNotEmpty()) {
            viewModel.checkedTagList.value.single().tagName.let {
                viewBinding.newTagInput.setText(it)
            }
        }
*/
    }

    override fun onResume() {
        super.onResume()

        viewBinding.newTagInput.requestFocus()
        viewLifecycleOwner.lifecycleScope.launch {
            delay(300)
            showKeyboard(viewBinding.newTagInput)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewBinding.newTagInput.text?.clear()
    }
}