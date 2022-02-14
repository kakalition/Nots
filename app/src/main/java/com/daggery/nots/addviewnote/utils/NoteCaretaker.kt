package com.daggery.nots.addviewnote.utils

import com.daggery.nots.addviewnote.data.NoteMemento

class NoteCaretaker {
    private var pointer = 0

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
}