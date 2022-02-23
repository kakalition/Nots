package com.daggery.domain.usecases.note

import com.daggery.domain.repositories.NotesRepository

interface BaseGetNoteUseCase {
    suspend operator fun invoke(id: String)
}