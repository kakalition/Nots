package com.daggery.domain.usecases.tag

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository

class DeleteTagByIdUseCase(private val tagsRepository: TagsRepository) : BaseDeleteTagByIdUseCase{
    override suspend operator fun invoke(id: Int) = tagsRepository.deleteTagById(id)
}