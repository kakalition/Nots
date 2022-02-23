package com.daggery.data.repositories.tags

import com.daggery.domain.entities.NoteTag
import kotlinx.coroutines.flow.Flow

internal interface TagsLocalDataSource {

    suspend fun getTagsFlow(): Flow<List<NoteTag>>

    suspend fun getTags(): List<NoteTag>

    suspend fun getTagById(id: Int): NoteTag

    suspend fun addTag(noteTag: NoteTag)

    suspend fun updateTag(noteTag: NoteTag)

    suspend fun updateTags(noteTags: List<NoteTag>)

    suspend fun deleteTag(noteTag: NoteTag)

    suspend fun deleteTagById(id: Int)

    suspend fun deleteTags(noteTag: List<NoteTag>)
}