package com.daggery.nots.home.view

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.daggery.nots.data.NoteTag
import com.daggery.nots.databinding.FragmentNewTagsBottomSheetBinding
import com.daggery.nots.home.viewmodel.ManageTagsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Implement Edit Tag

@AndroidEntryPoint
class AddEditTagBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "NewTagsDialog"
    }

    private var _viewBinding: FragmentNewTagsBottomSheetBinding? = null
    val viewBinding get() = _viewBinding!!

    private val viewModel: ManageTagsViewModel by activityViewModels()

    private fun showKeyboard(view: View) {
        val inputMethodManager = requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentNewTagsBottomSheetBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.confirmButton.setOnClickListener {
            if(!viewModel.isEditingTag()) {
                val tag = viewBinding.newTagInput.text.toString()
                if(tag.isNotBlank()) {
                    viewModel.addTag(NoteTag(tagName = tag, checked = false))
                    this.dismiss()
                } else {
                    Snackbar.make(viewBinding.root, "Tag is empty", Snackbar.LENGTH_SHORT)
                        .setAnchorView(viewBinding.newTagInput)
                        .show()
                }
            } else {
                // TODO: Handle existing note
                val tag = viewBinding.newTagInput.text.toString()
                Log.d("LOL tagname", tag.toString())
                if(tag.isNotBlank()) {
                    viewModel.getEditTag().let {
                        viewModel.editTag(it.copy(tagName = tag))
                        this.dismiss()
                    }
                } else {
                    Snackbar.make(viewBinding.root, "Tag is empty", Snackbar.LENGTH_SHORT)
                        .setAnchorView(viewBinding.newTagInput)
                        .show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(viewModel.checkedTagList.value.isNotEmpty()) {
            viewModel.checkedTagList.value.single().tagName.let {
                viewBinding.newTagInput.setText(it)
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
    }
}