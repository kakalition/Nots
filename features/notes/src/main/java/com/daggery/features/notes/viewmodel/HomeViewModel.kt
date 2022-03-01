package com.daggery.nots.home.viewmodel

import androidx.lifecycle.*
import com.daggery.data.common.DbNoteResult
import com.daggery.data.usecases.note.ReorderNotesChronologicallyUseCase
import com.daggery.domain.entities.NoteData
import com.daggery.domain.entities.NoteTag
import com.daggery.data.usecases.note.DeleteAllNotesUseCase
import com.daggery.data.usecases.note.GetNotesFlowUseCase
import com.daggery.data.usecases.note.RearrangeNotesOrderUseCase
import com.daggery.data.usecases.tag.GetTagsUseCase
import com.daggery.data.usecases.tag.UpdateTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNotesFlowUseCase: GetNotesFlowUseCase,
    private val rearrangeNotesOrderUseCase: RearrangeNotesOrderUseCase,
    private val reorderNotesChronologicallyUseCase: ReorderNotesChronologicallyUseCase,
    private val deleteAllNotesUseCase: DeleteAllNotesUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val updateTagsUseCase: UpdateTagsUseCase
) : ViewModel() {

    // Get all notes
    private val _allNotes = MutableStateFlow<DbNoteResult>(DbNoteResult.Loading)
    val allNotes = _allNotes.asStateFlow()

    init {
        viewModelScope.launch {
            getNotesFlowUseCase()
                .catch { _allNotes.emit(DbNoteResult.Error(it)) }
                .collect { _allNotes.emit(DbNoteResult.Success(it)) }
        }
    }

    suspend fun getAllTags(): List<NoteTag> {
        var value: List<NoteTag>? = null
        viewModelScope.launch {
            value = getTagsUseCase()
        }

        while (value == null) {
            delay(50)
        }

        return listOf()
    }

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

    fun updateTagByTagName(checkedTags: List<String>) {
        viewModelScope.launch {
            val updatedTagList = getAllTags().map {
                return@map if (checkedTags.contains(it.tagName)) {
                    it.copy(checked = true)
                } else it.copy(checked = false)
            }

            updateTagsUseCase(updatedTagList)
        }
    }
}
