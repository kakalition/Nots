package com.daggery.nots.addviewnote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggery.domain.entities.NoteData
import com.daggery.domain.usecases.note.*
import com.daggery.nots.addviewnote.data.NoteValidity
import com.daggery.nots.utils.NoteDateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddViewNoteViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    inner class NoteCache {
        private var _value: NoteData? = null
        val value = _value
        fun updateCache(title: String? = null, body: String? = null, tags: List<String>? = null) {
            _value = value?.copy(
                noteTitle = title ?: value.noteTitle,
                noteBody = body ?: value.noteBody,
                noteTags = tags ?: value.noteTags
            )
        }
        fun saveNoteCache(note: NoteData) { _value = note }
        fun deleteNoteCache() { _value = null }
    }

    private var _noteCache = NoteCache()
    val noteCache get() = _noteCache

    internal val noteDateUtils = NoteDateUtils()

    suspend fun getNote(uuid: String): NoteData {
        return getNoteByIdUseCase(uuid)
    }

    private suspend fun getUpperIndex(): Int {
        var upperIndex = -1
        val allNotes = getNotesUseCase()
        if (allNotes.isEmpty()) {
            upperIndex = 0
        } else {
            allNotes.forEach { note ->
                if (note.noteOrder >= upperIndex) {
                    upperIndex = note.noteOrder + 1
                }
            }
        }

        return upperIndex
    }

    suspend fun getBlankNote(): NoteData {
        return NoteData(
            uuid = UUID.randomUUID().toString(),
            priority = 0,
            noteOrder = getUpperIndex(),
            noteTitle = "",
            noteBody = "",
            noteDate = noteDateUtils.getRawCurrentDate(),
            noteTags = listOf()
        )
    }

    fun assertNoteValidity(note: NoteData): NoteValidity {
        val titleNotBlank = note.noteTitle.isNotBlank()
        val bodyNotBlank = note.noteBody.isNotBlank()
        return when {
            !titleNotBlank && !bodyNotBlank -> NoteValidity.TITLE_BODY_EMPTY
            !titleNotBlank -> { NoteValidity.TITLE_EMPTY }
            !bodyNotBlank -> { NoteValidity.BODY_EMPTY }
            titleNotBlank && bodyNotBlank -> { NoteValidity.VALID }
            else -> NoteValidity.UNKNOWN
        }
    }

    fun addNote(note: NoteData) {
        viewModelScope.launch {
            addNoteUseCase(note)
        }
    }

    fun updateNote(note: NoteData) {
        viewModelScope.launch {
            updateNoteUseCase(note)
        }
    }

    fun deleteNote(note: NoteData) {
        viewModelScope.launch {
            deleteNoteUseCase(note)
        }
    }

}
