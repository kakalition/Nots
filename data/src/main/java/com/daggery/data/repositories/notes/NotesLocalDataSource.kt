package com.daggery.data.repositories.notes

import com.daggery.domain.entities.NoteData
import kotlinx.coroutines.flow.Flow

internal interface NotesLocalDataSource {

    suspend fun getNotes(): Flow<List<NoteData>>

    suspend fun getNoteById(id: String): NoteData

    suspend fun rearrangeNotesOrder(modifiedList: List<NoteData>)

    suspend fun addNote(noteData: NoteData)

    suspend fun updateNote(noteData: NoteData)

    suspend fun deleteNote(noteData: NoteData)

    suspend fun deleteAllNotes()
}