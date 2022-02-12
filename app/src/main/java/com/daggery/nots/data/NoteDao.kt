package com.daggery.nots.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY priority DESC, note_order DESC")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * from notes WHERE uuid = :uuid")
    fun getNote(uuid: String): Flow<Note>

    @Update
    suspend fun rearrangeNoteOrder(notes: List<Note>)

    @Insert
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}