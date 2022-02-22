package com.daggery.domain.usecases

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.TagsRepository

class DeleteTagsUseCase(private val tagsRepository: TagsRepository) {
    suspend operator fun invoke(tagList: List<NoteTag>) = tagsRepository.deleteTags(tagList)
}