package com.daggery.data.db

import androidx.room.*
import com.daggery.data.entities.NoteTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteTagDao {

    @Query("SELECT * FROM tags ORDER BY tag_name ASC")
    fun getTags(): Flow<List<NoteTagEntity>>

    @Query("SELECT * FROM tags WHERE tag_name = :tagName")
    suspend fun getTagByTagName(tagName: String): NoteTagEntity

    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getTagById(id: Int): NoteTagEntity

    @Insert
    suspend fun addTag(noteTag: NoteTagEntity)

    @Update
    suspend fun updateTag(noteTag: NoteTagEntity)

    @Update
    suspend fun updateTags(noteTags: List<NoteTagEntity>)

    @Delete
    suspend fun deleteTag(noteTag: NoteTagEntity)

    @Query("DELETE FROM tags WHERE id = :id")
    suspend fun deleteTagById(id: Int)

    @Delete
    suspend fun deleteTags(noteTag: List<NoteTagEntity>)
}