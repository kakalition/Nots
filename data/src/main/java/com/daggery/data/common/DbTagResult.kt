package com.daggery.data.common

import com.daggery.domain.entities.NoteData
import com.daggery.domain.entities.NoteTag

sealed class DbTagResult {
    data class Success(val data: List<NoteTag>?) : DbTagResult()
    data class Loading(val data: List<NoteTag>? = null) : DbTagResult()
    data class Error(val exception: Exception) : DbTagResult()
}