package com.daggery.features.tags.data

data class NoteTagWithStatus(
    val id: Int,
    val tagName: String,
    var isSelected: Boolean,
    val onClickListener: (List<NoteTagWithStatus>, NoteTagWithStatus) -> Unit
)
