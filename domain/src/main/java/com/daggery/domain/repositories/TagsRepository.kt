package com.daggery.domain.repositories

import com.daggery.domain.entities.NoteTag
import kotlinx.coroutines.flow.Flow

interface TagsRepository {

    suspend fun getTags(): Flow<List<NoteTag>>

    suspend fun getTagById(id: Int): NoteTag

    suspend fun addTag(noteTag: NoteTag)

    suspend fun updateTag(noteTag: NoteTag)

    suspend fun updateTags(noteTags: List<NoteTag>)

    suspend fun deleteTag(noteTag: NoteTag)

    suspend fun deleteTagById(id: Int)

    suspend fun deleteTags(noteTag: List<NoteTag>)
}