package com.daggery.nots.home.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.daggery.nots.data.Note
import com.daggery.nots.data.NoteDao
import com.daggery.nots.data.NotsDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteDao: NoteDao
) : ViewModel() {

    // Get all notes
    val notes: Flow<List<Note>> = noteDao.getNotes()

    // Change note order of corresponding note
    // TODO: Could be optimized using upper and lower bound of index
    fun rearrangeNoteOrder(notes: MutableList<Note>) {
        viewModelScope.launch {
            noteDao.rearrangeNoteOrder(notes)
        }
    }

    // Change note priority to active
    fun prioritize(note: Note) {
        viewModelScope.launch {
            noteDao.updateNote(note.copy(priority = 1))
        }
    }

    // Change note priority to inactive
    fun unprioritize(note: Note) {
        viewModelScope.launch {
            noteDao.updateNote(note.copy(priority = 0))
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            noteDao.deleteAllNotes()
        }
    }
}
