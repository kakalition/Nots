package com.daggery.nots.home.utils

import android.graphics.Color
import android.view.MenuItem
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.home.view.HomeFragment
import com.daggery.nots.home.view.HomeFragmentDirections
import com.daggery.nots.utils.NotsVibrator
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragmentUtils(
    private val fragment: HomeFragment,
    private val navController: NavController
) {
    val notsVibrator = NotsVibrator(fragment.requireActivity())

    val setVerticalScrollState: (state: Boolean) -> Unit = { state ->
        fragment.notesLinearLayoutManager?.changeScrollState(state)
    }

    val noteClickListener: (Note) -> Unit = { note ->
        val uuid = note.uuid
        val action = HomeFragmentDirections.actionHomeFragmentToAddViewNoteFragment(uuid = uuid)
        fragment.findNavController().navigate(action)
    }

    val fabOnClickListener = { _ : View ->
        val extras = FragmentNavigatorExtras(fragment.viewBinding.fab to "from_fab_to_add")
        navController.navigate(HomeFragmentDirections.actionHomeFragmentToAddViewNoteFragment(uuid = ""), extras)
    }

    val onMenuItemClickListener: (MenuItem) -> Boolean = { item: MenuItem ->
        when(item.itemId) {
            R.id.reorder_button -> {
                true
            }
            R.id.settings_button -> {
                val destination = HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
                fragment.findNavController().navigate(destination)
                true
            }
            else -> false
        }
    }

    fun prioritize(note: Note) {
        fragment.viewModel.prioritize(note)
    }

    fun unprioritize(note: Note) {
        fragment.viewModel.unprioritize(note)
    }

    fun showDeleteDialog(note: Note) {
        MaterialAlertDialogBuilder(
            fragment.requireContext(),
            R.style.NotsAlertDialog
        )
            .setView(R.layout.dialog_delete)
            .setPositiveButton("Delete") { _, _ ->
                fragment.viewModel.deleteNote(note)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun getSwipeBgColor(): Int {
        return ColorUtils.setAlphaComponent(
            MaterialColors.getColor(
                fragment.requireContext(),
                com.google.android.material.R.attr.colorPrimary,
                Color.parseColor("#FFFFFFFF")
            ),
            200
        )
    }
    // Conditionally display empty illustration and notes list
    fun changeHomeState(isNotesEmpty: Boolean) {
        if(isNotesEmpty) {
            fragment.viewBinding.apply {
                emptyNotesLayout.visibility = View.VISIBLE
                notesRecyclerview.visibility = View.GONE
            }
        } else {
            fragment.viewBinding.apply {
                emptyNotesLayout.visibility = View.GONE
                notesRecyclerview.visibility = View.VISIBLE
            }
        }
    }
}
