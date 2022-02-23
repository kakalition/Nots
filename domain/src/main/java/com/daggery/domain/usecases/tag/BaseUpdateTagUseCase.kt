package com.daggery.domain.usecases.tag

import com.daggery.domain.entities.NoteTag

interface BaseUpdateTagUseCase {
    suspend operator fun invoke(noteTag: NoteTag)
}