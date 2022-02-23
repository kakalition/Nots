package com.daggery.domain.usecases.note

import com.daggery.domain.entities.NoteData
import kotlinx.coroutines.flow.Flow

interface BaseGetNotesUseCase {
    suspend operator fun invoke(): Flow<List<NoteData>>
}