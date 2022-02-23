package com.daggery.domain.usecases.note

import com.daggery.domain.entities.NoteData
import com.daggery.domain.repositories.NotesRepository
import javax.inject.Inject

class RearrangeNotesOrderUseCase @Inject constructor(private val notesRepository: NotesRepository) : BaseRearrangeNotesOrderUseCase {
    override suspend operator fun invoke(modifiedList: List<NoteData>) =
        notesRepository.rearrangeNotesOrder(modifiedList)
}