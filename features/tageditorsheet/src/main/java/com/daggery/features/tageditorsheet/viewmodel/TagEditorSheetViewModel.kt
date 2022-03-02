package com.daggery.features.tageditorsheet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggery.data.usecases.tag.AddTagUseCase
import com.daggery.data.usecases.tag.UpdateTagUseCase
import com.daggery.domain.entities.NoteTag
import com.daggery.domain.usecases.tag.GetTagByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagEditorSheetViewModel @Inject constructor(
    private val getTagByIdUseCase: GetTagByIdUseCase,
    private val addTagUseCase: AddTagUseCase,
    private val updateTagUseCase: UpdateTagUseCase
) : ViewModel() {

    private var tagItem: NoteTag? = null

    fun loadTagById(value: Int) {
        viewModelScope.launch {
            tagItem = getTagByIdUseCase(value)
        }
    }

    fun clearTagId() {
        tagItem = null
    }

    fun getTagItemName(): String {
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

/*
    fun isEditingTag() = checkedTagList.value.size == 1

    fun getEditTag() = checkedTagList.value.single()
*/

}