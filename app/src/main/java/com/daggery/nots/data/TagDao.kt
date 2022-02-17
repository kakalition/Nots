package com.daggery.nots.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Query("SELECT tag FROM tags ORDER BY tag DESC")
    fun getTags(): Flow<List<String>>

    @Insert
    suspend fun addTag(tag: Tag)

    @Update
    suspend fun editTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)
}