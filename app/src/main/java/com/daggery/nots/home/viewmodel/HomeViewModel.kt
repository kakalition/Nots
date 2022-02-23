package com.daggery.nots.home.viewmodel

import androidx.lifecycle.*
import com.daggery.data.common.DbNoteResult
import com.daggery.data.usecases.note.ReorderNotesChronologicallyUseCase
import com.daggery.domain.entities.NoteData
import com.daggery.domain.usecases.note.DeleteAllNotesUseCase
import com.daggery.domain.usecases.note.GetNotesUseCase
import com.daggery.domain.usecases.note.RearrangeNotesOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val rearrangeNotesOrderUseCase: RearrangeNotesOrderUseCase,
    private val reorderNotesChronologicallyUseCase: ReorderNotesChronologicallyUseCase,
    private val deleteAllNotesUseCase: DeleteAllNotesUseCase
) : ViewModel() {

    // Get all notes
    private val _allNotes = MutableStateFlow<DbNoteResult>(DbNoteResult.Loading)
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

    fun reorderNotesChronologically() {
        viewModelScope.launch {
            reorderNotesChronologicallyUseCase()
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            deleteAllNotesUseCase()
        }
    }
}
