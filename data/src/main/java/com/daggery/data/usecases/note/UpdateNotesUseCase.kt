package com.daggery.data.usecases.note

import com.daggery.domain.entities.NoteData
import com.daggery.domain.repositories.NotesRepository
import javax.inject.Inject

class UpdateNotesUseCase @Inject constructor(private val notesRepository: NotesRepository) {
    suspend operator fun invoke(notes: List<NoteData>) = notesRepository.updateNotes(notes)
}
