package com.daggery.nots.home.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggery.nots.data.NoteTag
import com.daggery.nots.data.NoteTagDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.internal.throwArrayMissingFieldException
import javax.inject.Inject

data class ManageTagsNoteTag(
    val id: Int,
    val tagName: String,
    var isSelected: Boolean,
    val onClickListener: (List<ManageTagsNoteTag>, ManageTagsNoteTag) -> Unit
)

@HiltViewModel
class ManageTagsViewModel @Inject constructor(
    private val dao: NoteTagDao
) : ViewModel() {

    private var _manageTagsList = MutableStateFlow<List<ManageTagsNoteTag>>(listOf())
    val manageTagsList get() = _manageTagsList.asStateFlow()

    private var _checkedTagsList = MutableStateFlow<List<ManageTagsNoteTag>>(listOf())
    val checkedTags get() = _checkedTagsList.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                dao.getTags().collect {
                    _manageTagsList.emit(
                        it.map { note ->
                            ManageTagsNoteTag(note.id, note.tagName, false) { list, noteTag -> select(list.toMutableList(), noteTag) }
                        }
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

    fun select(list: MutableList<ManageTagsNoteTag>, noteTag: ManageTagsNoteTag) {
        viewModelScope.launch {
            list[list.indexOf(noteTag)] = noteTag.copy(isSelected = !noteTag.isSelected)
            _manageTagsList.emit(list)
        }
    }

    fun deleteTags() {
        viewModelScope.launch {
            val noteTag = mutableListOf<Deferred<NoteTag>>()

            for(checkedTag in checkedTags.value) {
                noteTag.add(async { dao.getTagByTagName(checkedTag.tagName) })
            }
            dao.deleteTags(noteTag.awaitAll())
        }
    }
}