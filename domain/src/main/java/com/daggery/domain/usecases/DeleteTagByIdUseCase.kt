package com.daggery.domain.usecases

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository

class DeleteTagByIdUseCase(private val tagsRepository: TagsRepository) {
    suspend operator fun invoke(id: Int) = tagsRepository.deleteTagById(id)
}