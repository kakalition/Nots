package com.daggery.nots.home.view

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.daggery.nots.data.NoteTag
import com.daggery.nots.databinding.FragmentNewTagsDialogBinding
import com.daggery.nots.home.viewmodel.FilterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewTagsDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "NewTagsDialog"
    }

    private var _viewBinding: FragmentNewTagsDialogBinding? = null
    val viewBinding get() = _viewBinding!!

    private val viewModel: FilterViewModel by activityViewModels()

    private fun showKeyboard(view: View) {
        val inputMethodManager = requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentNewTagsDialogBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.confirmButton.setOnClickListener {
            val tag = viewBinding.newTagInput.text.toString()
            if(tag.isNotBlank()) {
                viewModel.addTag(NoteTag(tagName = tag, checked = false))
                this.dismiss()
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