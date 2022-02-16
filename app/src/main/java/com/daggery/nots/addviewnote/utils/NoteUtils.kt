package com.daggery.nots.addviewnote.utils

import android.text.Editable
import com.daggery.nots.addviewnote.data.NoteMemento
import com.daggery.nots.addviewnote.view.AddViewNoteFragment
import com.daggery.nots.observeOnce

// TODO: Create Empty Note getter

class NoteUtils(private val fragment: AddViewNoteFragment) {

    private val editableFactory = Editable.Factory()

    var noteTitle: String = ""
    var noteBody: String = ""


    internal fun bindsFields(uuid: String) {
        // Initial Fields
        with(fragment) {
            viewModel.getNote(uuid).observeOnce(viewLifecycleOwner) {
                noteTitle = it?.noteTitle ?: ""
                noteBody = it?.noteBody ?: ""

                viewBinding.noteTitle.text = editableFactory.newEditable(noteTitle)
                viewBinding.noteDate.text = fragment.viewModel.noteDateUtils.getParsedDate(
                    it?.noteDate ?: fragment.viewModel.noteDateUtils.getRawCurrentDate()
                )
                viewBinding.noteBody.text = editableFactory.newEditable(noteBody)
            }
        }

    }

}