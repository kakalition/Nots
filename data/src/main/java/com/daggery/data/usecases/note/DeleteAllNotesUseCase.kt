package com.daggery.domain.usecases.note

import com.daggery.domain.entities.NoteData
import com.daggery.domain.repositories.NotesRepository

class DeleteAllNotesUseCase(private val notesRepository: NotesRepository): BaseDeleteAllNotesUseCase {
    override suspend operator fun invoke() = notesRepository.deleteAllNotes()
}