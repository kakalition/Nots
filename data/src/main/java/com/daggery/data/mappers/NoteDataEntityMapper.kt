package com.daggery.data.mappers

import com.daggery.data.entities.NoteDataEntity
import com.daggery.domain.entities.NoteData
import javax.inject.Inject

internal class NoteDataEntityMapper @Inject constructor(){
    fun toNoteDataEntity(noteData: NoteData): NoteDataEntity {
        return NoteDataEntity(
            noteData.uuid,
            noteData.priority,
            noteData.noteOrder,
            noteData.noteTitle,
            noteData.noteBody,
            noteData.noteDate,
            noteData.noteTags
        )
    }

    fun toNoteData(noteDataEntity: NoteDataEntity): NoteData {
        return NoteData(
            noteDataEntity.uuid,
            noteDataEntity.priority,
            noteDataEntity.noteOrder,
            noteDataEntity.noteTitle,
            noteDataEntity.noteBody,
            noteDataEntity.noteDate,
            noteDataEntity.noteTags
        )
    }
}