package com.daggery.domain.usecases

import com.daggery.domain.entities.NoteData
import com.daggery.domain.repositories.NotesRepository

class UpdateNoteUseCase(private val notesRepository: NotesRepository) {
    suspend operator fun invoke(noteData: NoteData) = notesRepository.updateNote(noteData)
}