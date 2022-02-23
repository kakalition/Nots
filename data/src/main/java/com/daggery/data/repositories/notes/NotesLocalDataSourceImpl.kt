package com.daggery.data.repositories.notes

import com.daggery.data.db.NoteDao
import com.daggery.data.di.IoDispatcher
import com.daggery.data.mappers.NoteDataEntityMapper
import com.daggery.domain.entities.NoteData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class NotesLocalDataSourceImpl @Inject constructor(
    private val noteDao: NoteDao,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val noteDataEntityMapper: NoteDataEntityMapper
) : NotesLocalDataSource {

    override suspend fun getNotes(): Flow<List<NoteData>> {
        return noteDao.getNotes().map {
            it.map { noteDataEntity -> noteDataEntityMapper.toNoteData(noteDataEntity) }
        }
    }

    override suspend fun getNoteById(id: String): NoteData {
        return withContext(coroutineDispatcher) {
            return@withContext noteDataEntityMapper.toNoteData(noteDao.getNoteById(id))
        }
    }

    override suspend fun rearrangeNotesOrder(modifiedList: List<NoteData>) =
        withContext(coroutineDispatcher) {
            noteDao.rearrangeNoteOrder(
                modifiedList.map {
                    noteDataEntityMapper.toNoteDataEntity(it)
                }
            )
        }

    override suspend fun addNote(noteData: NoteData) = withContext(coroutineDispatcher) {
        noteDao.addNote(
            noteDataEntityMapper.toNoteDataEntity(noteData)
        )
    }

    override suspend fun updateNote(noteData: NoteData) = withContext(coroutineDispatcher) {
        noteDao.updateNote(
            noteDataEntityMapper.toNoteDataEntity(noteData)
        )
    }

    override suspend fun deleteNote(noteData: NoteData) = withContext(coroutineDispatcher) {
        noteDao.deleteNote(
            noteDataEntityMapper.toNoteDataEntity(noteData)
        )
    }

    override suspend fun deleteAllNotes() = withContext(coroutineDispatcher) {
        noteDao.deleteAllNotes()
    }
}
