package com.daggery.domain.repositories

import com.daggery.domain.entities.NoteData
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun getNotesFlow(): Flow<List<NoteData>>

    suspend fun getNotes(): List<NoteData>

    suspend fun getNoteById(id: String): NoteData

    suspend fun reorderNotesChronologically()

    suspend fun rearrangeNotesOrder(modifiedList: List<NoteData>)

    suspend fun addNote(noteData: NoteData)

    suspend fun updateNote(noteData: NoteData)

    suspend fun updateNotes(notes: List<NoteData>)

    suspend fun deleteNote(noteData: NoteData)

    suspend fun deleteAllNotes()
}