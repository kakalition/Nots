package com.daggery.domain.usecases.note

import com.daggery.domain.entities.NoteData
import com.daggery.domain.repositories.NotesRepository

interface BaseDeleteNoteUseCase {
    suspend operator fun invoke(noteData: NoteData)
}