package com.daggery.nots.home.utils

import android.graphics.Color
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.home.adapter.NotesItemTouchHelper
import com.daggery.nots.home.view.HomeFragment
import com.daggery.nots.home.view.HomeFragmentDirections
import com.daggery.nots.observeOnce
import com.daggery.nots.utils.NotsVibrator
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

    val notsVibrator = NotsVibrator(fragment.requireActivity())

    val setVerticalScrollState: (state: Boolean) -> Unit = { state ->
        fragment.notesLinearLayoutManager?.changeScrollState(state)
    }

    val noteClickListener: (Note) -> Unit = { note ->
        val uuid = note.uuid
        val action = HomeFragmentDirections.actionHomeFragmentToAddViewNoteFragment(uuid = uuid)
        fragment.findNavController().navigate(action)
    }

    private val fabOnClickListener = { _ : View ->
        val extras = FragmentNavigatorExtras(fragment.viewBinding.fab to "from_fab_to_add")
        navController.navigate(HomeFragmentDirections.actionHomeFragmentToAddViewNoteFragment(uuid = ""), extras)
    }

    private val onMenuItemClickListener: (MenuItem) -> Boolean = { item: MenuItem ->
        when(item.itemId) {
            R.id.reorder_button -> {
                showReorderChronologicallyDialog()
                true
            }
            R.id.delete_all_notes_button -> {
                showDeleteAllDialog()
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

    private fun reorderChronologically() {
        with(fragment.viewModel) {
            notes.observeOnce(fragment.viewLifecycleOwner) {
                val initialNotes = it.sortedBy { note -> note.noteDate }.toMutableList()
                val mappedNotes = initialNotes.mapIndexed { index, note ->
                    note.copy(noteOrder = index)
                }

                rearrangeNoteOrder(mappedNotes.toMutableList())
            }
        }
    }

    fun getHomeLayoutKey(): Int {
        return fragment.mainViewModel.themeManager.homeLayoutKey
    }

    fun bindsToolbar() {
        fragment.viewBinding.toolbarBinding.apply {
/*
            toolbarTitle.text = fragment.resources.getString(R.string.fragment_home_toolbar)
*/
            toolbar.inflateMenu(R.menu.menu_home_fragment)
            toolbar.setOnMenuItemClickListener(onMenuItemClickListener)
        }
    }

    fun bindsRecyclerView() {
        fragment.notesLiveData = fragment.viewModel.notes
        fragment.notesLiveData.observe(fragment.viewLifecycleOwner, fragment.notesObserver)
        fragment.viewBinding.notesRecyclerview.apply {
            layoutManager = fragment.notesLinearLayoutManager
            adapter = fragment.notesAdapter
        }

        val itemTouchHelper = ItemTouchHelper(NotesItemTouchHelper(fragment.notesAdapter!!))
        itemTouchHelper.attachToRecyclerView(fragment.viewBinding.notesRecyclerview)
    }

    fun bindsFab() {
        fragment.viewBinding.fab.setOnClickListener(fabOnClickListener)
    }

    fun rearrangeNoteOrder(notes: MutableList<Note>) {
        fragment.viewModel.rearrangeNoteOrder(notes)
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
            .setView(R.layout.dialog_delete_note)
            .setPositiveButton("Delete") { _, _ ->
                fragment.viewModel.deleteNote(note)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDeleteAllDialog() {
        MaterialAlertDialogBuilder(
            fragment.requireContext(),
            R.style.NotsAlertDialog
        )
            .setView(R.layout.dialog_delete_all_notes)
            .setPositiveButton("Delete") { _, _ ->
                fragment.viewModel.deleteAllNotes()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun showReorderChronologicallyDialog() {
        MaterialAlertDialogBuilder(
            fragment.requireContext(),
            R.style.NotsAlertDialog
        )
            .setView(R.layout.dialog_reorder_notes_chronologically)
            .setPositiveButton("Reorder") { _, _ ->
                reorderChronologically()
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

    fun getDeleteBgColor(): Int {
        return fragment.resources.getColor(R.color.red_pastel, null)
    }

    fun getPrioritizeColor(): Int {
        return fragment.resources.getColor(R.color.blue_pastel, null)
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
