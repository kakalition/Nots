package com.daggery.domain.usecases.note

import com.daggery.domain.entities.NoteData
import com.daggery.domain.repositories.NotesRepository

interface BaseRearrangeNotesOrderUseCase {
    suspend operator fun invoke(modifiedList: List<NoteData>)
}