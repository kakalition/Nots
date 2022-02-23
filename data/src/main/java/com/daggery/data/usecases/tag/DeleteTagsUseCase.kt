package com.daggery.domain.usecases.tag

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.TagsRepository

class DeleteTagsUseCase(private val tagsRepository: TagsRepository) : BaseDeleteTagsUseCase {
    override suspend operator fun invoke(tagList: List<NoteTag>) = tagsRepository.deleteTags(tagList)
}