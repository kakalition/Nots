package com.daggery.domain.usecases

import com.daggery.domain.repositories.NotesRepository

class GetNoteUseCase(private val notesRepository: NotesRepository) {
    suspend operator fun invoke(id: String) = notesRepository.getNoteById(id)
}