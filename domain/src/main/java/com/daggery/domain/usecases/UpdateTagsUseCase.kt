package com.daggery.domain.usecases

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.TagsRepository

class UpdateTagsUseCase(private val tagsRepository: TagsRepository) {
    suspend operator fun invoke(updatedTagsList: List<NoteTag>) = tagsRepository.updateTags(updatedTagsList)
}