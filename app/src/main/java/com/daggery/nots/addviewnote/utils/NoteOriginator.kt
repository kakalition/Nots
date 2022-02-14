package com.daggery.nots.addviewnote.utils

import com.daggery.nots.addviewnote.data.NoteMemento

class NoteOriginator {

    var noteTitle: String = ""
    var noteBody: String = ""
    var cursorPosition = 0

    fun createMemento(): NoteMemento {
        return NoteMemento(noteTitle, noteBody, cursorPosition)
    }

    fun setMemento(memento: NoteMemento) {
        noteTitle = memento.noteTitle
        noteBody = memento.noteBody
        cursorPosition = memento.cursorPosition
    }
}