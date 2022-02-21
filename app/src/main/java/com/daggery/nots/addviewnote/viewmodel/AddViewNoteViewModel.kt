package com.daggery.nots.addviewnote.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.daggery.nots.data.Note
import com.daggery.nots.data.NoteDao
import com.daggery.nots.data.NotsDatabase
import com.daggery.nots.observeOnce
import com.daggery.nots.utils.NoteDateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddViewNoteViewModel @Inject constructor(
    private val noteDao: NoteDao
) : ViewModel() {

    private var _noteCache: Note? = null
    val noteCache get() = _noteCache
    fun updateCache(title: String? = null, body: String? = null, tags: List<String>? = null) {
        _noteCache = _noteCache?.copy(
            noteTitle = title ?: noteCache!!.noteTitle,
            noteBody = body ?: noteCache!!.noteBody,
            noteTags = tags ?: noteCache!!.noteTags
        )
        Log.d("LOL update cache", noteCache.toString())
    }
    fun saveNoteCache(note: Note) { _noteCache = note }
    fun deleteNoteCache() { _noteCache = null }

    val noteDateUtils = NoteDateUtils()
    val notes: Flow<List<Note>> = noteDao.getNotes()

    /**
     * Get note with given uuid
     */
    fun getNote(uuid: String): Flow<Note> {
        return noteDao.getNote(uuid)
    }

    fun getStaticNote(uuid: String): Note {
        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking noteDao.getStaticNote(uuid)
        }
    }

    suspend fun getUpperIndex(): Int {
        var upperIndex = -1
        viewModelScope.launch {
            notes.collect {
                // Get upper index
                if (it.isEmpty()) {
                    upperIndex = 0
                } else {
                    it.forEach { note ->
                        if (note.noteOrder >= upperIndex) {
                            upperIndex = note.noteOrder + 1
                        }
                    }
                }
            }
        }

        while(upperIndex == -1) {
            delay(50)
        }

        return upperIndex
    }

    fun getBlankNote(): Flow<Note> {
        return flow {
            emit(
                Note(
                    uuid = UUID.randomUUID().toString(),
                    priority = 0,
                    noteOrder = getUpperIndex(),
                    noteTitle = "",
                    noteBody = "",
                    noteDate = noteDateUtils.getRawCurrentDate(),
                    noteTags = listOf()
                )
            )
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteDao.addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteDao.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }

}