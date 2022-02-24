package com.daggery.data.usecases.tag

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.TagsRepository
import javax.inject.Inject

class DeleteTagsUseCase @Inject constructor(private val tagsRepository: TagsRepository) {
    suspend operator fun invoke(tagList: List<NoteTag>) = tagsRepository.deleteTags(tagList)
}