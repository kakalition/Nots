package com.daggery.domain.usecases.tag

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.TagsRepository
import javax.inject.Inject

class GetTagByIdUseCase @Inject constructor(private val tagsRepository: TagsRepository) {
    suspend operator fun invoke(id: Int): NoteTag = tagsRepository.getTagById(id)
}