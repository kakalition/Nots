package com.daggery.domain.usecases

import com.daggery.domain.repositories.NotesRepository

class GetNotesUseCase(private val notesRepository: NotesRepository) {
    suspend operator fun invoke() = notesRepository.getNotes()
}