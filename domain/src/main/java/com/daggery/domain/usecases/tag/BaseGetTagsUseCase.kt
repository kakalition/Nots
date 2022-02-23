package com.daggery.domain.usecases.tag

import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository

interface BaseGetTagsUseCase {
    suspend operator fun invoke()
}