package com.daggery.data.repositories.notes

import com.daggery.domain.entities.NoteData
import com.daggery.domain.repositories.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class NotesRepositoryImpl @Inject constructor(private val notesLocalDataSource: NotesLocalDataSource)
    : NotesRepository {

    override suspend fun getNotesFlow(): Flow<List<NoteData>> {
        return notesLocalDataSource.getNotesFlow()
    }

    override suspend fun getNotes(): List<NoteData> {
        return notesLocalDataSource.getNotes()
    }

    override suspend fun getNoteById(id: String): NoteData {
        return notesLocalDataSource.getNoteById(id)
    }

    override suspend fun reorderNotesChronologically() {
        notesLocalDataSource.reorderChronologically()
    }

    override suspend fun rearrangeNotesOrder(modifiedList: List<NoteData>) {
        return notesLocalDataSource.rearrangeNotesOrder(modifiedList)
    }

    override suspend fun addNote(noteData: NoteData) {
        notesLocalDataSource.addNote(noteData)
    }

    override suspend fun updateNote(noteData: NoteData) {
        notesLocalDataSource.updateNote(noteData)
    }

    override suspend fun updateNotes(notes: List<NoteData>) {
        notesLocalDataSource.updateNotes(notes)
    }

    override suspend fun deleteNote(noteData: NoteData) {
        notesLocalDataSource.deleteNote(noteData)
    }

    override suspend fun deleteAllNotes() {
        notesLocalDataSource.deleteAllNotes()
    }
}