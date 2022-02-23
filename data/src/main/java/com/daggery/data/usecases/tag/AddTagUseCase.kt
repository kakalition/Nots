package com.daggery.domain.usecases.tag

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository
import javax.inject.Inject

class AddTagUseCase @Inject constructor(private val tagsRepository: TagsRepository) : BaseAddTagUseCase {
    override suspend operator fun invoke(noteTag: NoteTag) = tagsRepository.addTag(noteTag)
}