package com.daggery.data.usecases.tag

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTagsFlowUseCase @Inject constructor(private val tagsRepository: TagsRepository) {
    suspend operator fun invoke(): Flow<List<NoteTag>> = tagsRepository.getTagsFlow()
}