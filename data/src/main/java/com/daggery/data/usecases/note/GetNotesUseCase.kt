package com.daggery.domain.usecases.note

import com.daggery.domain.repositories.NotesRepository
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(private val notesRepository: NotesRepository) : BaseGetNotesUseCase {
    override suspend operator fun invoke() = notesRepository.getNotes()
}