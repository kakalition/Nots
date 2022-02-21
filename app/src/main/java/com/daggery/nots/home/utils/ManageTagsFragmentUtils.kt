package com.daggery.nots.home.utils

import android.graphics.Color
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.home.view.ManageTagsFragment
import com.daggery.nots.home.view.NewTagBottomSheetFragment
import com.google.android.material.color.MaterialColors

// TODO: Implements RecyclerView.Selection
class ManageTagsFragmentUtils(private val fragment: ManageTagsFragment) {

    private val menuItemClickListener = { item: MenuItem ->
        when(item.itemId) {
            R.id.add_tags_button -> {
                fragment.newTagsDialog.show(
                    fragment.requireActivity().supportFragmentManager,
                    NewTagBottomSheetFragment.TAG
                )
                true
            }
            else -> false
        }
    }

    val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.menu_filter_fragment_action_mode, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when(item?.itemId) {
                R.id.edit_button -> { true }
                R.id.delete_button -> { true }
                else -> { false }
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            fragment.actionMode = null
        }

    }

    internal fun bindsToolbar() {
        with(fragment.viewBinding.toolbarBinding.toolbar) {
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { fragment.findNavController().navigateUp() }
            inflateMenu(R.menu.menu_filter_fragment)
            menu.findItem(R.id.add_tags_button).icon.setTint(
                MaterialColors.getColor(
                    fragment.requireContext(),
                    com.google.android.material.R.attr.colorOnSurface,
                    Color.parseColor("#FF212121")
                )
            )
            setOnMenuItemClickListener(menuItemClickListener)
            title = "Manage Tags"
        }
    }
}