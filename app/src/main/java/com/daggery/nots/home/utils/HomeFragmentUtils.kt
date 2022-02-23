package com.daggery.nots.home.utils

import android.graphics.Color
import android.view.MenuItem
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.daggery.domain.entities.NoteData
import com.daggery.nots.R
import com.daggery.nots.home.adapter.NotesItemTouchHelper
import com.daggery.nots.home.view.HomeFragment
import com.daggery.nots.home.view.HomeFragmentDirections
import com.daggery.nots.home.view.TagsFilterBottomSheetFragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragmentUtils(
    private val fragment: HomeFragment,
    private val navController: NavController
) {

    val outlinedTextColor = MaterialColors.getColor(
        fragment.requireContext(),
        com.google.android.material.R.attr.colorOnSurface,
        fragment.resources.getColor(R.color.white, null)
    )
    fun getHomeLayoutKey(): Int {
        return fragment.mainViewModel.themeManager.homeLayoutKey
    }

    fun showDeleteDialog(note: NoteData) {
        MaterialAlertDialogBuilder(
            fragment.requireContext(),
            R.style.NotsAlertDialog
        )
            .setView(R.layout.dialog_delete_note)
            .setPositiveButton("Delete") { _, _ ->
                //fragment.viewModel.deleteNote(note)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
