package com.daggery.features.tageditorsheet.view

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.daggery.features.tageditorsheet.databinding.FragmentTagEditorSheetBinding
import com.daggery.features.tageditorsheet.viewmodel.TagEditorSheetViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Test: Is AddTagUseCase works as expected

@AndroidEntryPoint
class TagEditorSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "NewTagsDialog"
    }

    private var _viewBinding: FragmentTagEditorSheetBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: TagEditorSheetViewModel by activityViewModels()

    var id: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentTagEditorSheetBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            id?.let {
                loadTagById(it)
                while(!viewModel.tagItemRetrieved) { delay(50) }
                viewBinding.newTagInput.text = Editable.Factory().newEditable(viewModel.getTagItemName())
            }
        }

        viewBinding.confirmButton.setOnClickListener {

            val tag = viewBinding.newTagInput.text.toString()
            when {
                tag.isBlank() -> {
                    Snackbar.make(viewBinding.root, "Tag is empty", Snackbar.LENGTH_SHORT)
                        .setAnchorView(viewBinding.newTagInput)
                        .show()
                }

                tag.isNotBlank() && !viewModel.isTagItemAvailable() -> {
                    viewModel.addTag(tag)
                    this.dismiss()
                }

                tag.isNotBlank() && viewModel.isTagItemAvailable() -> {
                    viewModel.updateTag(tag)
                    this.dismiss()
                }
            }
        }
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
        viewModel.cleanUp()
    }

    fun loadTagById(value: Int) {
        viewModel.loadTagById(value)
    }

    private fun showKeyboard(view: View) {
        val inputMethodManager = requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }
}