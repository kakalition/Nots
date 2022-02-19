package com.daggery.nots.addviewnote.utils

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.addviewnote.view.AddViewNoteFragment
import com.daggery.nots.addviewnote.view.AssignTagsBottomSheetFragment
import com.daggery.nots.data.Note
import com.google.android.material.snackbar.Snackbar

class AddViewNoteFragmentUtils(
    private val fragment: AddViewNoteFragment,
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

    private val navigationClickListener: (View) -> Unit = {
        saveNote()
    }

    private val onMenuItemClickListener: (MenuItem) -> Boolean = { item: MenuItem ->
        when (item.itemId) {
            R.id.delete_button -> {
                if(fragment.args.uuid.isBlank()) {
                    fragment.findNavController().navigateUp()
                } else {
                    // TODO: FINISH THIS
                    fragment.viewModel.noteCache?.let {
                        fragment.viewModel.deleteNote(it)
                    }
                    fragment.findNavController().navigateUp()
                }
                true
            }
            R.id.assign_tags -> {
                fragment.assignTagsBottomSheetFragment.assignTagNameList(fragment.viewModel.noteCache?.noteTags ?: listOf())
                fragment.assignTagsBottomSheetFragment.show(
                    fragment.parentFragmentManager,
                    AssignTagsBottomSheetFragment.TAG
                )
                true
            }
            else -> false
        }
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(fragment.viewBinding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun assertNoteValidityThenAddNote(note: Note): Boolean {
        val titleNotBlank = note.noteTitle.isNotBlank()
        val bodyNotBlank = note.noteBody.isNotBlank()
        return when {
            !titleNotBlank && !bodyNotBlank -> true
            !titleNotBlank -> {
                showSnackBar("Title is empty")
                false
            }
            !bodyNotBlank -> {
                showSnackBar("Body is empty")
                false
            }
            titleNotBlank && bodyNotBlank -> {
                fragment.viewModel.addNote(note)
                true
            }
            else -> true
        }
    }

    fun saveNote() {
        val newNote = fragment.viewModel.noteCache?.copy(
            noteTitle = fragment.viewBinding.noteTitle.text.toString(),
            noteBody = fragment.viewBinding.noteBody.text.toString()
        )
        newNote?.let {
            if(fragment.args.uuid.isNotBlank()) {
                fragment.viewModel.updateNote(it)
                fragment.findNavController().navigateUp()
            } else {
                when(assertNoteValidityThenAddNote(newNote)) {
                    true -> {
                        fragment.viewModel.deleteNoteCache()
                        fragment.findNavController().navigateUp()
                    }
                    else -> {}
                }
            }
        }
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
            noteTitle.requestFocus()
            // TODO: Add slight delay
            showKeyboard(noteTitle)
        }
    }
}

