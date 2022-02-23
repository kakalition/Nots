package com.daggery.domain.usecases.tag

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository
import javax.inject.Inject

class DeleteTagByIdUseCase @Inject constructor(private val tagsRepository: TagsRepository) : BaseDeleteTagByIdUseCase{
    override suspend operator fun invoke(id: Int) = tagsRepository.deleteTagById(id)
}