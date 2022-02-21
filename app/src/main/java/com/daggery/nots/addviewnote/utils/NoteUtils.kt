package com.daggery.nots.addviewnote.utils

import android.text.Editable
import android.util.Log
import android.view.View
import com.daggery.nots.R
import com.daggery.nots.addviewnote.view.AddViewNoteFragment
import com.daggery.nots.data.Note
import com.google.android.material.chip.Chip

class NoteUtils(private val fragment: AddViewNoteFragment) {

    private val editableFactory = Editable.Factory()

    internal fun bindsChips(chipsName: List<String>) {
        if(chipsName.isEmpty()) {
            fragment.viewBinding.chipGroup.visibility = View.GONE
        } else {
            fragment.viewBinding.chipGroup.visibility = View.VISIBLE
            fragment.viewBinding.chipGroup.removeAllViews()
            chipsName.forEach { noteTag ->
                val chip = fragment.layoutInflater.inflate(R.layout.chip_note_item, fragment.viewBinding.chipGroup, false) as Chip
                chip.text = noteTag
                chip.isCheckable = false
                chip.isClickable = false
                chip.isFocusable = false
                // TODO: Check this behavior
                // TODO: Ensure when checking, chipgroup layout doesn't change
                // chip.ensureAccessibleTouchTarget(48)
                fragment.viewBinding.chipGroup.addView(chip)
            }
        }
    }

    internal fun bindsFields(note: Note?, from: String) {
        Log.d("LOL binds from $from", note.toString())
        with(fragment.viewBinding) {
            noteTitle.text = editableFactory.newEditable(note?.noteTitle ?: "")
            noteDate.text = editableFactory.newEditable(
                fragment.viewModel.noteDateUtils.getParsedDate(
                    note?.noteDate ?: fragment.viewModel.noteDateUtils.getRawCurrentDate()
                )
            )
            noteBody.text = editableFactory.newEditable(note?.noteBody ?: "")
            bindsChips(note?.noteTags ?: listOf())
        }
    }
}