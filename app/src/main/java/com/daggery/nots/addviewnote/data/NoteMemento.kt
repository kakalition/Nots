package com.daggery.nots.addviewnote.data

data class NoteMemento(
    val noteTitle: String,
    val noteBody: String,
    val cursorPosition: Int
)