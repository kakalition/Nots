package com.daggery.nots.addviewnote.utils

import android.app.Activity
import android.graphics.Color
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.home.view.AddViewNoteFragment
import com.daggery.nots.home.view.AddViewNoteFragmentArgs
import com.daggery.nots.home.view.UneditedNote
import com.daggery.nots.observeOnce
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform

class AddViewNoteFragmentUtils(
    private val fragment: AddViewNoteFragment,
    private val args: AddViewNoteFragmentArgs
) {

    val onConfirmTapped = {
        val noteTitle = fragment.viewBinding.noteTitle.text.toString()
        val noteBody = fragment.viewBinding.noteBody.text.toString()

        val isNoteInvalid = noteTitle.isBlank() && noteBody.isBlank()
        val isUuidValid = args.uuid.isNotBlank()

        when {
            isNoteInvalid -> { showFailToAddSnackBar() }
            isUuidValid -> {
                val noteLiveData = fragment.viewModel.getNote(args.uuid)
                noteLiveData.observeOnce(fragment) { observedNote ->
                    val note = observedNote?.copy(noteTitle = noteTitle, noteBody = noteBody)
                    note?.let {
                        fragment.viewModel.updateNote(it)
                        viewEnvironment()
                    }
                }
            }
            else -> {
                fragment.viewModel.addNote(noteTitle, noteBody)
                fragment.findNavController().navigateUp()
            }
        }
    }

    val onEditTapped = {
        val noteBody = fragment.viewBinding.noteBody
        fragment.isEditing = true
        editEnvironment()

        // Show Keyboard with Pointer at The End of Text
        noteBody.requestFocus()
        noteBody.setSelection(noteBody.text?.length ?: 0)
        showKeyboard(noteBody)
    }

    private val navigationClickListener: (View) -> Unit = {
        if(fragment.isEditing) {
            showOnRevertConfirmation(fragment.uneditedNote)
        }
        else fragment.findNavController().navigateUp()
    }

    private val onMenuItemClickListener: (MenuItem) -> Boolean = { item: MenuItem ->
        when(item.itemId) {
            R.id.confirm_button -> {
                onConfirmTapped()
                true
            }
            R.id.edit_button -> {
                onEditTapped()
                true
            }
            // TODO: Decide
            R.id.delete_button -> {
                true
            }
            else -> false
        }
    }

    internal fun showOnRevertConfirmation(uneditedNote: UneditedNote) {
        val isNoteTitleSame = fragment.viewBinding.noteTitle.text.toString() == uneditedNote.noteTitle
        val isNoteBodySame = fragment.viewBinding.noteBody.text.toString() == uneditedNote.noteBody
        if(isNoteTitleSame && isNoteBodySame) {
            viewEnvironment()
        } else {
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
    }

    private fun showFailToAddSnackBar() {
        val snackbar = Snackbar.make(
            fragment.viewBinding.addViewNoteRoot,
            "Failed to add note. Fields cannot be blank.",
            2000
        )
        snackbar.show()
    }

    internal fun bindsToolbar() {
        fragment.viewBinding.toolbarBinding.toolbar.apply {
            inflateMenu(R.menu.menu_add_view_fragment)
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener(navigationClickListener)
            setOnMenuItemClickListener(onMenuItemClickListener)
        }
    }

    internal fun bindsFields(uuid: String) {
        if(uuid.isBlank()) {
            val note = fragment.viewModel.getNewNote()
            fragment.viewBinding.apply {
                noteTitle.text = fragment.editableFactory.newEditable(note.noteTitle)
                noteDate.text = fragment.editableFactory.newEditable(note.noteDate)
                noteBody.text = fragment.editableFactory.newEditable(note.noteBody)
            }
        } else {
            val noteLiveData = fragment.viewModel.getNote(uuid)
            noteLiveData.observeOnce(fragment.viewLifecycleOwner) {
                it?.let {
                    fragment.uneditedNote = UneditedNote(it.noteTitle, it.noteBody)
                    fragment.viewBinding.apply {
                        noteTitle.text = fragment.editableFactory.newEditable(it.noteTitle)
                        noteDate.text = fragment.editableFactory.newEditable(it.noteDate)
                        noteBody.text = fragment.editableFactory.newEditable(it.noteBody)
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

    internal fun addEnvironment() {
        fragment.viewBinding.apply {
            toolbarBinding.toolbarTitle.text = "New Note"
            noteTitle.isEnabled = true
            noteBody.isEnabled = true
        }
        setMenuVisibility(
            confirmButton = true,
            editButton = false,
            deleteButton = false
        )
    }

    internal fun viewEnvironment() {
        fragment.isEditing = false
        fragment.viewBinding.apply {
            toolbarBinding.toolbarTitle.text = "View"
            noteTitle.isEnabled = false
            noteBody.isEnabled = false
        }
        setMenuVisibility(
            confirmButton = false,
            editButton = true,
            deleteButton = true
        )
    }

    internal fun editEnvironment() {
        fragment.isEditing = true
        fragment.viewBinding.apply {
            toolbarBinding.toolbarTitle.text = "Edit"
            noteTitle.isEnabled = true
            noteBody.isEnabled = true
        }
        setMenuVisibility(
            confirmButton = true,
            editButton = false,
            deleteButton = false
        )
    }

    private fun setMenuVisibility(confirmButton: Boolean, editButton: Boolean, deleteButton: Boolean) {
        fragment.viewBinding.toolbarBinding.toolbar.menu.apply {
            findItem(R.id.confirm_button).isVisible = confirmButton
            findItem(R.id.edit_button).isVisible = editButton
            findItem(R.id.delete_button).isVisible = deleteButton
        }
    }

    private fun revertChanges(uneditedNote: UneditedNote) {
        fragment.viewBinding.apply {
            noteTitle.text = fragment.editableFactory.newEditable(uneditedNote.noteTitle)
            noteBody.text = fragment.editableFactory.newEditable(uneditedNote.noteBody)
        }
        viewEnvironment()
    }

}
