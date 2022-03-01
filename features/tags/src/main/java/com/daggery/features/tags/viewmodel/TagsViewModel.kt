package com.daggery.features.tags.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggery.data.usecases.tag.AddTagUseCase
import com.daggery.data.usecases.tag.DeleteTagsUseCase
import com.daggery.data.usecases.tag.GetTagsFlowUseCase
import com.daggery.data.usecases.tag.UpdateTagUseCase
import com.daggery.domain.entities.NoteTag
import com.daggery.domain.usecases.tag.*
import com.daggery.features.tags.data.NoteTagWithStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

// TODO: Maybe I Can Use Cache

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val getTagsFlowUseCase: GetTagsFlowUseCase,
    private val getTagByIdUseCase: GetTagByIdUseCase,
    private val addTagUseCase: AddTagUseCase,
    private val updateTagUseCase: UpdateTagUseCase,
    private val deleteTagsUseCase: DeleteTagsUseCase
) : ViewModel() {

    private var _manageTagsList = MutableStateFlow<List<NoteTagWithStatus>>(listOf())
    val manageTagsList get() = _manageTagsList.asStateFlow()

    private var _checkedTagsList = MutableStateFlow<List<NoteTagWithStatus>>(listOf())
    val checkedTagList get() = _checkedTagsList.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                getTagsFlowUseCase().collect {
                    _manageTagsList.emit(
                        it.map { note -> note.toNoteTagWithStatus() }
                    )
                }
            }
            launch {
                manageTagsList.collect { list ->
                    _checkedTagsList.emit(list.filter { it.isSelected })
                }
            }
        }
    }

    private fun NoteTag.toNoteTagWithStatus(): NoteTagWithStatus {
        return NoteTagWithStatus(id, tagName, false) { list, noteTagWithStatus ->
            select(list.toMutableList(), noteTagWithStatus)
        }
    }

    private fun select(list: MutableList<NoteTagWithStatus>, noteTag: NoteTagWithStatus) {
        viewModelScope.launch {
            val index = list.indexOfFirst { it.id == noteTag.id }
            list[index] = noteTag.copy(isSelected = !noteTag.isSelected)
            _manageTagsList.emit(list)
        }
    }

    fun addTag(noteTag: NoteTag) {
        viewModelScope.launch { addTagUseCase(noteTag) }
    }

    fun updateTag(value: NoteTagWithStatus) {
        viewModelScope.launch {
            val sourceNoteTag = getTagByIdUseCase(value.id)
            updateTagUseCase(sourceNoteTag.copy(tagName = value.tagName))
        }
    }

    fun deleteTags() {
        viewModelScope.launch {
            val tagList = mutableListOf<Deferred<NoteTag>>()

            for(checkedTag in checkedTagList.value) {
                tagList.add(async { getTagByIdUseCase(checkedTag.id) })
            }

            deleteTagsUseCase(tagList.awaitAll())
        }
    }
}