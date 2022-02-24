package com.daggery.nots.managetags.data

data class NoteTagWithStatus(
    val id: Int,
    val tagName: String,
    var isSelected: Boolean,
    val onClickListener: (List<NoteTagWithStatus>, NoteTagWithStatus) -> Unit
)
