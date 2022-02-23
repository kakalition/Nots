package com.daggery.domain.usecases.note

import com.daggery.domain.repositories.NotesRepository
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(private val notesRepository: NotesRepository) : BaseGetNoteUseCase {
    override suspend operator fun invoke(id: String) = notesRepository.getNoteById(id)
}