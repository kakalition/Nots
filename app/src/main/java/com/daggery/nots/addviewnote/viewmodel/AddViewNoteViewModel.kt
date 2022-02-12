package com.daggery.nots.addviewnote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.daggery.nots.data.Note
import com.daggery.nots.data.NotsDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddViewNoteViewModel @Inject constructor(
    private val database: NotsDatabase
) : ViewModel() {

    private val noteDao = database.noteDao()
    val notes: LiveData<List<Note>> = noteDao.getNotes().asLiveData()

    fun getRawCurrentDate(): Int {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyymmdd")
        return formatter.format(date).toInt()
    }

    fun getParsedDate(rawDate: Int): String {
        val parser = SimpleDateFormat("yyyymmdd")
        val parsedDate = parser.parse(rawDate.toString())
        val formatter = SimpleDateFormat.getDateInstance()
        return formatter.format(parsedDate!!)
    }

    /**
     * Get note with given uuid
     */
    fun getNote(uuid: String): LiveData<Note?> {
        return database.noteDao().getNote(uuid).asLiveData()
    }

    fun getNewNote(): Note {
        return Note(
            uuid = UUID.randomUUID().toString(),
            priority = 0,
            noteOrder = notes.value?.size ?: 0,
            noteTitle = "",
            noteBody = "",
            noteDate = getRawCurrentDate()
        )
    }

    fun addNote(title: String, body: String) {
        val note = Note(
            uuid = UUID.randomUUID().toString(),
            priority = 0,
            noteOrder = notes.value?.size ?: 0,
            noteTitle = title,
            noteBody = body,
            noteDate = getRawCurrentDate()
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