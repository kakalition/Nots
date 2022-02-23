package com.daggery.data.common

import com.daggery.domain.entities.NoteData

sealed class DbNoteResult {
    data class Success(val data: List<NoteData>?) : DbNoteResult()
    object Loading: DbNoteResult()
    data class Error(val throwable: Throwable) : DbNoteResult()
}