package com.daggery.domain.usecases.note

import com.daggery.domain.repositories.NotesRepository

interface BaseGetNotesUseCase {
    suspend operator fun invoke()
}