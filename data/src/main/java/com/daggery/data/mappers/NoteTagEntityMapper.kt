package com.daggery.data.mappers

import com.daggery.data.entities.NoteTagEntity
import com.daggery.domain.entities.NoteTag

class NoteTagEntityMapper {
    fun toNoteTagEntity(noteTag: NoteTag): NoteTagEntity {
        return NoteTagEntity(
            noteTag.id,
            noteTag.tagName,
            noteTag.checked
        )
    }

    fun toNoteTag(noteTagEntity: NoteTagEntity): NoteTag {
        return NoteTag(
            noteTagEntity.id,
            noteTagEntity.tagName,
            noteTagEntity.checked
        )
    }
}