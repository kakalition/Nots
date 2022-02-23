package com.daggery.data.repositories.tags

import com.daggery.data.db.TagDao
import com.daggery.data.mappers.NoteTagEntityMapper
import com.daggery.domain.entities.NoteTag
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class TagsLocalDataSourceImpl @Inject constructor(
    private val tagDao: TagDao,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val tagEntityMapper: NoteTagEntityMapper
) : TagsLocalDataSource {

    override suspend fun getTags(): Flow<List<NoteTag>> {
        return tagDao.getTags().map {
            it.map { noteTagEntity -> tagEntityMapper.toNoteTag(noteTagEntity) }
        }
    }

    override suspend fun getTagById(id: Int): NoteTag  =
        withContext(coroutineDispatcher) {
            return@withContext tagEntityMapper.toNoteTag(tagDao.getTagById(id))
        }

    override suspend fun addTag(noteTag: NoteTag) =
        withContext(coroutineDispatcher) {
            tagDao.addTag(
                tagEntityMapper.toNoteTagEntity(noteTag)
            )
        }

    override suspend fun updateTag(noteTag: NoteTag) =
        withContext(coroutineDispatcher) {
            tagDao.updateTag(
                tagEntityMapper.toNoteTagEntity(noteTag)
            )
        }

    override suspend fun updateTags(noteTags: List<NoteTag>) =
        withContext(coroutineDispatcher) {
            tagDao.updateTags(
                noteTags.map { tagEntityMapper.toNoteTagEntity(it) }
            )
        }

    override suspend fun deleteTag(noteTag: NoteTag) =
        withContext(coroutineDispatcher) {
            tagDao.deleteTag(
                tagEntityMapper.toNoteTagEntity(noteTag)
            )
        }

    override suspend fun deleteTagById(id: Int) =
        withContext(coroutineDispatcher) {
            tagDao.deleteTagById(id)
        }

    override suspend fun deleteTags(noteTag: List<NoteTag>) =
        withContext(coroutineDispatcher) {
            tagDao.deleteTags(
                noteTag.map { tagEntityMapper.toNoteTagEntity(it) }
            )
        }
}