package com.daggery.nots.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteTagDao {

    @Query("SELECT * FROM tags ORDER BY tag_name ASC")
    fun getTags(): Flow<List<NoteTag>>

    @Query("SELECT * FROM tags WHERE tag_name = :tagName")
    suspend fun getTagByTagName(tagName: String): NoteTag

    @Insert
    suspend fun addTag(noteTag: NoteTag)

    @Update
    suspend fun editTag(noteTag: NoteTag)

    @Update
    suspend fun updateTags(noteTags: List<NoteTag>)

    @Delete
    suspend fun deleteTag(noteTag: NoteTag)

    @Delete
    suspend fun deleteTags(noteTag: List<NoteTag>)
}