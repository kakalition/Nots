package com.daggery.domain.entities

data class NoteData(
    val uuid: String,
    val priority: Int,
    val noteOrder: Int,
    val noteTitle: String,
    val noteBody: String,
    val noteDate: Long,
    val noteTags: List<String>,
)