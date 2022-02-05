package com.daggery.nots.home.viewmodel

import androidx.annotation.StyleRes
import androidx.lifecycle.*
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.data.NotsDatabase
import com.daggery.nots.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val database: NotsDatabase,
    private val dataStore: DataStoreManager
) : ViewModel() {

    val themePref: LiveData<Int> = dataStore.themePreference.asLiveData()

    var _themeKey: Int = 0;
    val themeKey get() = _themeKey
    fun applyTheme(@StyleRes themeRes: Int) {
        _themeKey = themeRes
        viewModelScope.launch {
            dataStore.changeThemePreference(themeRes)
        }
    }

    // Get note dao
    private val noteDao = database.noteDao()
    // Get all notes
    val notes: LiveData<List<Note>> = noteDao.getNotes().asLiveData()

    private fun getCurrentDate(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("MMMM dd, yyyy")
        val formattedDate = formatter.format(date)
        return formattedDate
    }

    fun getNewNote(): Note {
        return Note(
            UUID.randomUUID().toString(),
            0,
            notes.value?.size ?: 0,
            "",
            "",
            getCurrentDate()
        )
    }

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

    fun addNote(
        title: String,
        body: String) {
        val note = Note(
            uuid = UUID.randomUUID().toString(),
            priority = 0,
            noteOrder = notes.value?.size ?: 0,
            noteTitle = title,
            noteBody = body,
            noteDate = getCurrentDate()
        )
        addNoteToDatabase(note)
    }

    private fun addNoteToDatabase(note: Note) {
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
/*

class HomeViewModelFactory(private val database: NotsDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}*/
