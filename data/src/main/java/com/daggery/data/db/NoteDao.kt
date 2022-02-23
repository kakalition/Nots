package com.daggery.data.db

import androidx.room.*
import com.daggery.data.entities.NoteDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY priority DESC, note_order DESC")
    fun getNotes(): Flow<List<NoteDataEntity>>

    @Query("SELECT * from notes WHERE uuid = :uuid")
    fun getNoteById(uuid: String): NoteDataEntity

    @Update
    suspend fun rearrangeNoteOrder(notes: List<NoteDataEntity>)

    @Insert
    suspend fun addNote(note: NoteDataEntity)

    @Update
    suspend fun updateNote(note: NoteDataEntity)

    @Delete
    suspend fun deleteNote(note: NoteDataEntity)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}