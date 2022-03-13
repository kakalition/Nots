package com.daggery.features.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import com.daggery.domain.entities.NoteData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {
    val dashboardNote = NoteData("skdlfndsf", 0, 9, "Title Dashboard", "Body Dashboard", 10000, listOf())
    val lastWrittenNotes = List(3) {
        NoteData(it.toString(), 0, it, "Title $it", "Body $it", 10000, listOf())
    }
}