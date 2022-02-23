package com.daggery.domain.usecases.tag

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository

class AddTagUseCase(private val tagsRepository: TagsRepository) {
    suspend operator fun invoke(noteTag: NoteTag) = tagsRepository.addTag(noteTag)
}