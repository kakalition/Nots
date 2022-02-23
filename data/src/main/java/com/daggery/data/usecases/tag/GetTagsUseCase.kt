package com.daggery.domain.usecases.tag

import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository
import javax.inject.Inject

class GetTagsUseCase @Inject constructor(private val tagsRepository: TagsRepository) {
    suspend operator fun invoke() = tagsRepository.getTags()
}