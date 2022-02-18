package com.daggery.nots.addviewnote.utils

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.addviewnote.view.AddViewNoteFragment
import com.daggery.nots.addviewnote.view.AddViewNoteFragmentArgs
import com.daggery.nots.addviewnote.view.AssignTagsBottomSheetFragment
import com.daggery.nots.observeOnce
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class AddViewNoteFragmentUtils(
    private val fragment: AddViewNoteFragment,
    private val args: AddViewNoteFragmentArgs
) {

    var titleHasFocus = false
    var bodyHasFocus = false

    val titleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        // Focus to note body when pressing enter while text line is two
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.count { it == '\n' } == 1) {
                fragment.viewBinding.noteBody.setSelection(fragment.viewBinding.noteBody.length())
                fragment.viewBinding.noteBody.requestFocus()
            }
        }

        // Remove last blank newline
        override fun afterTextChanged(s: Editable?) {
            if (s?.count { it == '\n' } == 1) {
                s.delete(s.lastIndexOf('\n'), s.length)
            }
        }
    }

    val onConfirmTapped = {
        val noteTitle = fragment.viewBinding.noteTitle.text.toString()
        val noteBody = fragment.viewBinding.noteBody.text.toString()

        val isNoteInvalid = noteTitle.isBlank() || noteBody.isBlank()

        when {
            isNoteInvalid -> { showFailToAddSnackBar() }
            else -> {
                fragment.viewModel.notes.observeOnce(fragment.viewLifecycleOwner) {
                    var upperIndex = -1

                    // Get upper index
                    if(it.isEmpty()) { upperIndex = 0 }
                    else {
                        it.forEach { note ->
                            if (note.noteOrder >= upperIndex) {
                                upperIndex = note.noteOrder + 1
                            }
                        }
                    }
                    fragment.viewModel.addNote(noteTitle, noteBody, upperIndex)
                    fragment.findNavController().navigateUp()
                }
            }
        }
    }

    private val navigationClickListener: (View) -> Unit = {
        if(fragment.isNewNote == true) {
            onBackPressedWhenNewNote()
        } else {
            updateNoteNavigateUp()
        }
    }

    private val onMenuItemClickListener: (MenuItem) -> Boolean = { item: MenuItem ->
        when(item.itemId) {
            R.id.confirm_button -> {
                onConfirmTapped()
                true
            }
            R.id.delete_button -> {
                true
            }
            R.id.assign_tags -> {
                fragment.assignTagsBottomSheetFragment.show(fragment.parentFragmentManager, AssignTagsBottomSheetFragment.TAG)
                true
            }
            else -> false
        }
    }

    private fun showUnsavedConfirmationDialog() {
        MaterialAlertDialogBuilder(
            fragment.requireContext(),
            R.style.NotsAlertDialog
        )
            .setView(R.layout.dialog_unsaved_confirmation)
            .setPositiveButton("Sure") { _, _ ->
                fragment.findNavController().navigateUp()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // TODO: Change to let scope resolution
    fun updateNoteNavigateUp() {
        fragment.viewModel.getNote(args.uuid).observeOnce(fragment.viewLifecycleOwner) {
            val newNote = it!!.copy(
                noteTitle = fragment.viewBinding.noteTitle.text.toString(),
                noteBody = fragment.viewBinding.noteBody.text.toString()
            )
            fragment.viewModel.updateNote(newNote)
            fragment.findNavController().navigateUp()
        }
    }

    fun onBackPressedWhenNewNote() {
        if(isNewNoteInvalid()) {
            fragment.findNavController().navigateUp()
        } else {
            showUnsavedConfirmationDialog()
        }
    }

    private fun isNewNoteInvalid(): Boolean {
        with(fragment.viewBinding) {
            return fragment.isNewNote == true && noteTitle.text.isNullOrBlank() && noteBody.text.isNullOrBlank()
        }
    }

    private fun showFailToAddSnackBar() {
        val snackBar = Snackbar.make(
            fragment.viewBinding.addViewNoteRoot,
            "Failed to add note. Fields cannot be blank.",
            2000
        )
        snackBar.show()
    }

    internal fun bindsToolbar() {
        fragment.viewBinding.toolbarBinding.toolbar.apply {
            inflateMenu(R.menu.menu_add_view_fragment)
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener(navigationClickListener)
            setOnMenuItemClickListener(onMenuItemClickListener)
        }
    }

    fun showKeyboard(view: View) {
        val inputMethodManager = fragment.requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    internal fun hideKeyboard(view: View) {
        val inputMethodManager = fragment.requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }

    internal fun clearNoteTypingFocus() {
        with(fragment.viewBinding) {
            noteTitle.clearFocus()
            noteBody.clearFocus()
        }
    }

    internal fun addEnvironment() {
        fragment.viewBinding.apply {
            noteTitle.isEnabled = true
            noteBody.isEnabled = true

            noteTitle.requestFocus()
            // TODO: Add slight delay
            showKeyboard(noteTitle)
        }
        setMenuVisibility(
            confirmButton = true,
            editButton = false,
            deleteButton = false
        )

    }

    internal fun editEnvironment() {
        fragment.viewBinding.apply {
            noteTitle.isEnabled = true
            noteBody.isEnabled = true
        }
        setMenuVisibility(
            confirmButton = false,
            editButton = false,
            deleteButton = true
        )
    }

    // TODO: Implement correct behavior
    private fun setMenuVisibility(confirmButton: Boolean, editButton: Boolean, deleteButton: Boolean) {
        fragment.viewBinding.toolbarBinding.toolbar.menu.apply {
            findItem(R.id.confirm_button).isVisible = confirmButton
            findItem(R.id.delete_button).isVisible = deleteButton
        }
    }

}
