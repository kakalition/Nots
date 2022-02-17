package com.daggery.nots.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggery.nots.data.NoteTag
import com.daggery.nots.data.NoteTagDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(private val noteTagDao: NoteTagDao) : ViewModel() {

    private val tagList = noteTagDao.getTags()

    fun getTag(tagName: String) {
        viewModelScope.launch {
            noteTagDao.getTag(tagName)
        }
    }

    fun addTag(noteTag: NoteTag) {
        viewModelScope.launch {
            noteTagDao.addTag(noteTag)
        }
    }

    fun deleteTag(noteTag: NoteTag) {
        viewModelScope.launch {
            noteTagDao.deleteTag(noteTag)
        }
    }

    fun editTag(noteTag: NoteTag) {
        viewModelScope.launch {
            noteTagDao.editTag(noteTag)
        }
    }
}