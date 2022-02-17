package com.daggery.nots.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteTagDao {

    @Query("SELECT tag FROM tags ORDER BY tag DESC")
    fun getTags(): Flow<List<String>>

    @Query("SELECT * FROM tags WHERE tag = :tagName")
    fun getTag(tagName: String): NoteTag

    @Insert
    suspend fun addTag(noteTag: NoteTag)

    @Update
    suspend fun editTag(noteTag: NoteTag)

    @Delete
    suspend fun deleteTag(noteTag: NoteTag)
}