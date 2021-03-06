package com.daggery.data.usecases.note

import com.daggery.domain.entities.NoteData
import com.daggery.domain.repositories.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesFlowUseCase @Inject constructor(private val notesRepository: NotesRepository) {
    suspend operator fun invoke(): Flow<List<NoteData>> = notesRepository.getNotesFlow()
}