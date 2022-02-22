package com.daggery.domain.repositories

import com.daggery.domain.entities.NoteData
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun getNotes(): Flow<List<NoteData>>

    suspend fun getNoteById(id: String): NoteData

    suspend fun rearrangeNotesOrder(modifiedList: List<NoteData>)

    suspend fun addNote(noteData: NoteData)

    suspend fun updateNote(noteData: NoteData)

    suspend fun deleteNote(noteData: NoteData)

    suspend fun deleteAllNotes()
}