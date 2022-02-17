package com.daggery.nots.home.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.lifecycleScope
import com.daggery.nots.databinding.FragmentNewTagsDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewTagsDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "NewTagsDialog"
    }

    private var _viewBinding: FragmentNewTagsDialogBinding? = null
    val viewBinding get() = _viewBinding!!

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

    }

    override fun onResume() {
        super.onResume()

        viewBinding.newTagInput.requestFocus()
        viewLifecycleOwner.lifecycleScope.launch {
            delay(300)
            showKeyboard(viewBinding.newTagInput)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}