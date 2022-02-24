package com.daggery.data.usecases.note

import com.daggery.domain.repositories.NotesRepository
import javax.inject.Inject

class ReorderNotesChronologicallyUseCase @Inject constructor(private val notesRepository: NotesRepository) {
    suspend operator fun invoke() = notesRepository.reorderNotesChronologically()
}