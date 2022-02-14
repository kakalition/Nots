package com.daggery.nots.addviewnote.utils

import android.app.Activity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.addviewnote.view.AddViewNoteFragment
import com.daggery.nots.addviewnote.view.AddViewNoteFragmentArgs
import com.daggery.nots.addviewnote.view.UneditedNote
import com.daggery.nots.data.Note
import com.daggery.nots.observeOnce
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

// TODO: Last Worked: Removing Modal

class AddViewNoteFragmentUtils(
    private val fragment: AddViewNoteFragment,
    private val args: AddViewNoteFragmentArgs
) {

    var titleHasFocus = false
    var bodyHasFocus = false

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
        val isKeyboardShown = titleHasFocus || bodyHasFocus
        if(fragment.isNewNote == true) {
            onBackPressedWhenNewNote()
        } else {
            updateNoteNavigateUp()
        }
/*
        with(fragment) {
            when {
                isKeyboardShown -> {
                    hideKeyboard(it)
                    clearNoteTypingFocus()
                }
                !isKeyboardShown -> {
                    // showOnRevertConfirmation(uneditedNote)
                    findNavController().navigateUp()
                }
                else -> findNavController().navigateUp()
            }
        }
*/
    }

    private val onMenuItemClickListener: (MenuItem) -> Boolean = { item: MenuItem ->
        when(item.itemId) {
            R.id.undo_button -> {
                true
            }
            R.id.redo_button -> {
                true
            }
            R.id.undo_all_button -> {
                true
            }
            R.id.confirm_button -> {
                onConfirmTapped()
                true
            }
            R.id.delete_button -> {
                true
            }
            else -> false
        }
    }

    internal fun showOnRevertConfirmation(uneditedNote: UneditedNote) {
        val isNoteTitleSame =
            fragment.viewBinding.noteTitle.text.toString() == uneditedNote.noteTitle
        val isNoteBodySame = fragment.viewBinding.noteBody.text.toString() == uneditedNote.noteBody
        MaterialAlertDialogBuilder(
            fragment.requireContext(),
            R.style.NotsAlertDialog
        )
            .setView(R.layout.dialog_revert_confirmation)
            .setPositiveButton("Revert") { _, _ ->
                revertChanges(uneditedNote)
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
            showOnRevertConfirmation(UneditedNote("A", "N"))
        }
    }

    fun isNewNoteInvalid(): Boolean {
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

    // TODO: Could be optimized
    internal fun bindsFields(uuid: String) {
        with(fragment) {
            if(uuid.isBlank()) {
                viewBinding.apply {
                    noteTitle.text = editableFactory.newEditable("")
                    noteDate.text = viewModel.noteDateUtils.getParsedDate(viewModel.noteDateUtils.getRawCurrentDate())
                    noteBody.text = editableFactory.newEditable("")
                }
            } else {
                viewModel.getNote(uuid).observeOnce(fragment.viewLifecycleOwner) {
                    it?.let {
                        val note = it
                        // uneditedNote = UneditedNote(it.noteTitle, it.noteBody)
                        viewBinding.apply {
                            noteTitle.text = editableFactory.newEditable(note.noteTitle)
                            noteDate.text = viewModel.noteDateUtils.getParsedDate(note.noteDate)
                            noteBody.text = editableFactory.newEditable(note.noteBody)
                        }
                    }
                }
            }
        }
    }

    private fun showKeyboard(view: View) {
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
/*            toolbarBinding.toolbarTitle.text = fragment.requireContext()
                .getString(R.string.toolbar_title_new_note)*/
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
/*            toolbarBinding.toolbarTitle.text = fragment.requireContext()
                .getString(R.string.toolbar_title_edit)*/
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
            findItem(R.id.undo_button).isVisible = true
            findItem(R.id.redo_button).isVisible = true
            findItem(R.id.undo_all_button).isVisible = true
            findItem(R.id.confirm_button).isVisible = confirmButton
            findItem(R.id.delete_button).isVisible = deleteButton
        }
    }

    private fun revertChanges(uneditedNote: UneditedNote) {
        fragment.viewBinding.apply {
            noteTitle.text = fragment.editableFactory.newEditable(uneditedNote.noteTitle)
            noteBody.text = fragment.editableFactory.newEditable(uneditedNote.noteBody)
        }
    }

}
