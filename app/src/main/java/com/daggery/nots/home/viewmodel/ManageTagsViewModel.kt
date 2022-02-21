package com.daggery.nots.home.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggery.nots.data.NoteTagDao
import dagger.hilt.android.lifecycle.HiltViewModel
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
    dao: NoteTagDao
) : ViewModel() {

    private var _manageTagsList = MutableStateFlow<List<ManageTagsNoteTag>>(listOf())
    val manageTagsList get() = _manageTagsList.asStateFlow()

    private var _isCheckedTagsEmpty = MutableStateFlow(true)
    val checkedTags get() = _isCheckedTagsEmpty.asStateFlow()


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
                    _isCheckedTagsEmpty.emit(list.map { manageTagsNoteTag -> manageTagsNoteTag.isSelected }
                        .any { it }
                    )
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
}