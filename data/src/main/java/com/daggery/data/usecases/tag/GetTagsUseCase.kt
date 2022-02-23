package com.daggery.domain.usecases.tag

import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository

class GetTagsUseCase(private val tagsRepository: TagsRepository) {
    suspend operator fun invoke() = tagsRepository.getTags()
}