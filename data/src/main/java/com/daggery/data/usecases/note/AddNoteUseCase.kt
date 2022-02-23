package com.daggery.domain.usecases.note

import com.daggery.domain.entities.NoteData
import com.daggery.domain.repositories.NotesRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(private val notesRepository: NotesRepository) : BaseAddNoteUseCase {
    override suspend operator fun invoke(noteData: NoteData) = notesRepository.addNote(noteData)
}