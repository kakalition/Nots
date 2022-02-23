package com.daggery.domain.usecases.note

import com.daggery.domain.entities.NoteData
import com.daggery.domain.repositories.NotesRepository

class RearrangeNotesOrderUseCase(private val notesRepository: NotesRepository) {
    suspend operator fun invoke(modifiedList: List<NoteData>) =
        notesRepository.rearrangeNotesOrder(modifiedList)
}