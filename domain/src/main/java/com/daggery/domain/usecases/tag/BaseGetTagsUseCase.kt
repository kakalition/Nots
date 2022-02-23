package com.daggery.domain.usecases.tag

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository
import kotlinx.coroutines.flow.Flow

interface BaseGetTagsUseCase {
    suspend operator fun invoke(): Flow<List<NoteTag>>
}