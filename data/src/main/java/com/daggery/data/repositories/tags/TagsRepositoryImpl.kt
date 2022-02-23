package com.daggery.data.repositories.tags

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.TagsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TagsRepositoryImpl
    @Inject constructor(private val tagsLocalDataSource: TagsLocalDataSource) : TagsRepository {

    override suspend fun getTagsFlow(): Flow<List<NoteTag>> {
        return tagsLocalDataSource.getTagsFlow()
    }

    override suspend fun getTags(): List<NoteTag> {
        return tagsLocalDataSource.getTags()
    }

    override suspend fun getTagById(id: Int): NoteTag {
        return tagsLocalDataSource.getTagById(id)
    }

    override suspend fun addTag(noteTag: NoteTag) {
        tagsLocalDataSource.addTag(noteTag)
    }

    override suspend fun updateTag(noteTag: NoteTag) {
        tagsLocalDataSource.updateTag(noteTag)
    }

    override suspend fun updateTags(noteTags: List<NoteTag>) {
        tagsLocalDataSource.updateTags(noteTags)
    }

    override suspend fun deleteTag(noteTag: NoteTag) {
        tagsLocalDataSource.deleteTag(noteTag)
    }

    override suspend fun deleteTagById(id: Int) {
        tagsLocalDataSource.deleteTagById(id)
    }

    override suspend fun deleteTags(noteTag: List<NoteTag>) {
        tagsLocalDataSource.deleteTags(noteTag)
    }
}