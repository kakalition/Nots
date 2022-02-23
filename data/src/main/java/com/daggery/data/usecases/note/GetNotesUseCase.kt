package com.daggery.domain.usecases.note

import com.daggery.domain.repositories.NotesRepository

class GetNotesUseCase(private val notesRepository: NotesRepository) : BaseGetNotesUseCase {
    override suspend operator fun invoke() = notesRepository.getNotes()
}