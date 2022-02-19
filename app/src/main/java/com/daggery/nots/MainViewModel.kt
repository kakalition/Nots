package com.daggery.nots

import android.util.Log
import androidx.annotation.StyleRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggery.nots.data.NoteDao
import com.daggery.nots.data.NoteTag
import com.daggery.nots.data.NoteTagDao
import com.daggery.nots.datastore.DataStoreManager
import com.daggery.nots.utils.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    val themeManager: ThemeManager,
    private val noteTagDao: NoteTagDao
) : ViewModel() {

    val tagList = noteTagDao.getTags()

    fun applyTheme(@StyleRes themeRes: Int) {
        viewModelScope.launch {
            dataStoreManager.changeThemePreference(themeRes)
        }
        themeManager.setThemeKey(themeRes)
    }

    fun applyLayout(layoutId: Int) {
        viewModelScope.launch {
            dataStoreManager.changeHomeLayoutPreference(layoutId)
        }
        themeManager.setHomeLayoutKey(layoutId)
    }

    private suspend fun getTag(tagName: String): NoteTag {
        val noteTag: NoteTag? = null
        viewModelScope.launch {
            noteTagDao.getTagByTagName(tagName)
        }
        while(noteTag == null) {
            delay(50)
        }
        return noteTag
    }

    fun addTag(noteTag: NoteTag) {
        viewModelScope.launch {
            noteTagDao.addTag(noteTag)
        }
    }

    fun updateTagByTagName(noteTagName: List<String>) {
        Log.d("LOL noteTagName", noteTagName.toString())
        viewModelScope.launch {
            var updated = false
            var updatedTagList = listOf<NoteTag>()
            async {
                tagList.collect { tagList ->
                    updatedTagList = tagList.map {
                        return@map if(noteTagName.contains(it.tagName)) {
                            it.copy(checked = true)
                        } else { it.copy(checked = false) }
                    }
                    updated = true
                }
            }

            while(!updated) {
                delay(50)
            }

            Log.d("LOL", updatedTagList.toString())
            noteTagDao.updateTags(updatedTagList)
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