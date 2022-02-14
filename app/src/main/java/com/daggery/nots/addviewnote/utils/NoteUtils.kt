package com.daggery.nots.addviewnote.utils

import android.text.Editable
import com.daggery.nots.addviewnote.data.NoteMemento
import com.daggery.nots.addviewnote.view.AddViewNoteFragment
import com.daggery.nots.observeOnce

// TODO: Create Empty Note getter

class NoteUtils(private val fragment: AddViewNoteFragment) {

    private val editableFactory = Editable.Factory()

    private var pointer = 0
    var noteTitle: String = ""
    var noteBody: String = ""
    var cursorPosition = 0

    var noteDate: String = ""

    val changeList = mutableListOf<NoteMemento>()

    fun addMemento(memento: NoteMemento) {
        changeList[pointer] = memento
        pointer++
    }

    fun undo() {
        pointer--
    }

    fun redo() {
        pointer++
    }

    fun createMemento(): NoteMemento {
        return NoteMemento(noteTitle, noteBody, cursorPosition)
    }

    fun setMemento(memento: NoteMemento) {
        noteTitle = memento.noteTitle
        noteBody = memento.noteBody
        cursorPosition = memento.cursorPosition
    }

    internal fun bindsFields(uuid: String) {
        // Initial Fields
        with(fragment) {
            viewModel.getNote(uuid).observeOnce(viewLifecycleOwner) {
                noteTitle = it?.noteTitle ?: ""
                noteBody = it?.noteBody ?: ""
                noteDate = viewModel.noteDateUtils.getParsedDate(
                    it?.noteDate ?: viewModel.noteDateUtils.getRawCurrentDate()
                )
                cursorPosition = it?.noteBody?.length ?: 0

                viewBinding.noteTitle.text = editableFactory.newEditable(noteTitle)
                viewBinding.noteDate.text = editableFactory.newEditable(noteDate)
                viewBinding.noteBody.text = editableFactory.newEditable(noteBody)
            }
        }

    }

}