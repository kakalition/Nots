package com.daggery.nots.addviewnote.utils

import android.text.Editable
import com.daggery.nots.R
import com.daggery.nots.addviewnote.view.AddViewNoteFragment
import com.daggery.nots.data.Note
import com.daggery.nots.observeOnce
import com.google.android.material.chip.Chip

class NoteUtils(private val fragment: AddViewNoteFragment) {

    private val editableFactory = Editable.Factory()

    private fun bindsChips(chipsName: List<String>) {
        chipsName.forEach { noteTag ->
            val chip = fragment.layoutInflater.inflate(R.layout.chip_filter, fragment.viewBinding.chipGroup, false) as Chip
            chip.text = noteTag
            // TODO: Check this behavior
            // TODO: Ensure when checking, chipgroup layout doesn't change
            // chip.ensureAccessibleTouchTarget(48)
            fragment.viewBinding.chipGroup.addView(chip)
        }
    }

    internal fun bindsFields(note: Note?) {
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