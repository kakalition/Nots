package com.daggery.nots.home.viewmodel

import androidx.lifecycle.*
import com.daggery.nots.data.Note
import com.daggery.nots.data.NotsDatabase
import kotlinx.coroutines.launch

class HomeViewModel(private val database: NotsDatabase) : ViewModel() {

    // Get note dao
    private val noteDao = database.noteDao()
    // Get all notes
    val notes: LiveData<List<Note>> = noteDao.getNotes().asLiveData()

    fun isPrioritized(note: Note): Boolean {
        return note.priority == 1
    }

    // Get note with given uuid
    fun getNote(uuid: String): LiveData<Note?> {
        return database.noteDao().getNote(uuid).asLiveData()
    }

    // Change note order of corresponding note
    fun rearrangeNoteOrder(currentNote: Note, targetNote: Note) {
        viewModelScope.launch {
            val notes = mutableListOf<Note>()
            notes.add(currentNote.copy(noteOrder = targetNote.noteOrder))
            notes.add(targetNote.copy(noteOrder = currentNote.noteOrder))
            database.noteDao().rearrangeNoteOrder(notes)
        }
    }

    // Change note priority to active
    fun prioritize(note: Note) {
        viewModelScope.launch {
            database.noteDao().updateNote(note.copy(priority = 1))
        }
    }

    // Change note priority to inactive
    fun unprioritize(note: Note) {
        viewModelScope.launch {
            database.noteDao().updateNote(note.copy(priority = 0))
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            database.noteDao().addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            database.noteDao().updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            database.noteDao().deleteNote(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            database.noteDao().deleteAllNotes()
        }
    }
}