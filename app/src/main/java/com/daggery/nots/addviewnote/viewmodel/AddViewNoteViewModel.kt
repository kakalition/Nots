package com.daggery.nots.addviewnote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.daggery.nots.data.Note
import com.daggery.nots.data.NotsDatabase
import com.daggery.nots.utils.NoteDateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddViewNoteViewModel @Inject constructor(
    private val database: NotsDatabase
) : ViewModel() {

    val noteDateUtils = NoteDateUtils()

    private val noteDao = database.noteDao()
    val notes: LiveData<List<Note>> = noteDao.getNotes().asLiveData()

    /**
     * Get note with given uuid
     */
    fun getNote(uuid: String): LiveData<Note?> {
        return database.noteDao().getNote(uuid).asLiveData()
    }

    // TODO: Two functions below could be optimized

    fun getNewNote(): Note {
        return Note(
            uuid = UUID.randomUUID().toString(),
            priority = 0,
            noteOrder = notes.value?.size ?: 0,
            noteTitle = "",
            noteBody = "",
            noteDate = noteDateUtils.getRawCurrentDate()
        )
    }

    fun addNote(title: String, body: String) {
        val note = Note(
            uuid = UUID.randomUUID().toString(),
            priority = 0,
            noteOrder = notes.value?.size ?: 0,
            noteTitle = title,
            noteBody = body,
            noteDate = noteDateUtils.getRawCurrentDate()
        )

        viewModelScope.launch {
            database.noteDao().addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            database.noteDao().updateNote(note)
        }
    }

}