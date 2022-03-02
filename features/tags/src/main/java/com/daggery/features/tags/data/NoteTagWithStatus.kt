package com.daggery.features.tags.data

data class NoteTagWithStatus(
    val id: Int,
    val tagName: String,
    var isSelected: Boolean,
    var tagCount: Int = 0,
    val onClickListener: (List<NoteTagWithStatus>, NoteTagWithStatus) -> Unit
)
