package com.daggery.features.tageditorsheet.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggery.data.usecases.note.GetNotesUseCase
import com.daggery.data.usecases.note.UpdateNotesUseCase
import com.daggery.data.usecases.tag.AddTagUseCase
import com.daggery.data.usecases.tag.UpdateTagUseCase
import com.daggery.domain.entities.NoteTag
import com.daggery.domain.usecases.tag.GetTagByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagEditorSheetViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val updateNotesUseCase: UpdateNotesUseCase,
    private val getTagByIdUseCase: GetTagByIdUseCase,
    private val addTagUseCase: AddTagUseCase,
    private val updateTagUseCase: UpdateTagUseCase
) : ViewModel() {

    private var tagItem: NoteTag? = null

    var tagItemRetrieved: Boolean = false

    fun loadTagById(value: Int) {
        viewModelScope.launch {
            tagItem = getTagByIdUseCase(value)
            tagItemRetrieved = true
        }
    }

    fun cleanUp() {
        tagItem = null
        tagItemRetrieved = false
    }

    fun getTagItemName(): String {
        Log.d("LOL TagItemName", tagItem?.tagName.toString())
        return tagItem?.tagName ?: ""
    }

    fun isTagItemAvailable(): Boolean {
        return tagItem != null
    }

    fun addTag(tagName: String) {
        viewModelScope.launch {
            addTagUseCase(NoteTag(0, tagName, false))
        }
    }

    fun updateTag(tagName: String) {
        viewModelScope.launch {
            tagItem?.let {
                updateTagUseCase(it.copy(tagName = tagName))
            }
        }
    }
}