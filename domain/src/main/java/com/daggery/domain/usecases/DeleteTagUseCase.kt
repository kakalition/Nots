package com.daggery.domain.usecases

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository

class DeleteTagUseCase(private val tagsRepository: TagsRepository) {
    suspend operator fun invoke(noteTag: NoteTag) = tagsRepository.deleteTag(noteTag)
}