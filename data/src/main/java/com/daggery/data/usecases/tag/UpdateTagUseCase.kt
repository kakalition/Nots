package com.daggery.data.usecases.tag

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.TagsRepository
import javax.inject.Inject

class UpdateTagUseCase @Inject constructor(private val tagsRepository: TagsRepository) {
    suspend operator fun invoke(noteTag: NoteTag) = tagsRepository.updateTag(noteTag)
}