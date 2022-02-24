package com.daggery.data.usecases.note

import com.daggery.domain.entities.NoteData
import com.daggery.domain.repositories.NotesRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(private val notesRepository: NotesRepository) {
    suspend operator fun invoke(id: String): NoteData = notesRepository.getNoteById(id)
}