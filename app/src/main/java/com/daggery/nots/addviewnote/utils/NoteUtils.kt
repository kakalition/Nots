package com.daggery.nots.addviewnote.utils

import android.text.Editable
import com.daggery.nots.addviewnote.view.AddViewNoteFragment
import com.daggery.nots.observeOnce

class NoteUtils(private val fragment: AddViewNoteFragment) {

    private val editableFactory = Editable.Factory()

    internal fun bindsFields(uuid: String) {
        // Initial Fields
        with(fragment) {
            viewModel.getNote(uuid).observeOnce(viewLifecycleOwner) {
                viewBinding.noteTitle.text = editableFactory.newEditable(it?.noteTitle ?: "")
                viewBinding.noteDate.text = editableFactory.newEditable(viewModel.noteDateUtils.getParsedDate(
                    it?.noteDate ?: viewModel.noteDateUtils.getRawCurrentDate()))
                viewBinding.noteBody.text = editableFactory.newEditable(it?.noteBody ?: "")
            }
        }
    }
}