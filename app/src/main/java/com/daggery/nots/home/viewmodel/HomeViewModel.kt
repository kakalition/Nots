package com.daggery.nots.home.viewmodel

import android.provider.ContactsContract
import androidx.lifecycle.*
import com.daggery.data.common.DbNoteResult
import com.daggery.data.common.DbResult
import com.daggery.data.entities.NoteDataEntity
import com.daggery.domain.entities.NoteData
import com.daggery.domain.usecases.note.DeleteAllNotesUseCase
import com.daggery.domain.usecases.note.GetNoteUseCase
import com.daggery.domain.usecases.note.GetNotesUseCase
import com.daggery.domain.usecases.note.RearrangeNotesOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val rearrangeNotesOrderUseCase: RearrangeNotesOrderUseCase,
    private val deleteAllNotesUseCase: DeleteAllNotesUseCase
) : ViewModel() {

    // Get all notes
    val _allNotes = MutableStateFlow<DbNoteResult>(DbNoteResult.Loading)
    val allNotes = _allNotes.asStateFlow()

    init {
        viewModelScope.launch {
            getNotesUseCase()
                .catch { _allNotes.emit(DbNoteResult.Error(it)) }
                .collect { _allNotes.emit(DbNoteResult.Success(it)) }
        }
    }


    // Change note order of corresponding note
    fun rearrangeNoteOrder(notes: List<NoteData>) {
        viewModelScope.launch {
            rearrangeNotesOrderUseCase(notes)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            deleteAllNotesUseCase()
        }
    }
}
