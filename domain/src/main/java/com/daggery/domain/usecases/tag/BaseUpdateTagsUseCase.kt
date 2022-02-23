package com.daggery.domain.usecases.tag

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.TagsRepository

interface BaseUpdateTagsUseCase {
    suspend operator fun invoke(updatedTagsList: List<NoteTag>)
}