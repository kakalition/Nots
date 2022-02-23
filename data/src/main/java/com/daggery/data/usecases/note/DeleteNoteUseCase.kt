package com.daggery.domain.usecases.note

import com.daggery.domain.entities.NoteData
import com.daggery.domain.repositories.NotesRepository

class DeleteNoteUseCase(private val notesRepository: NotesRepository) {
    suspend operator fun invoke(noteData: NoteData) = notesRepository.deleteNote(noteData)
}