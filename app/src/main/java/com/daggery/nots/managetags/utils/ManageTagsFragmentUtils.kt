package com.daggery.nots.managetags.utils

import android.graphics.Color
import android.view.MenuItem
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.home.view.ManageTagsFragment
import com.daggery.nots.managetags.view.AddEditTagBottomSheetFragment
import com.google.android.material.color.MaterialColors

// TODO: Implements RecyclerView.Selection
class ManageTagsFragmentUtils(private val fragment: ManageTagsFragment) {

    private val menuItemClickListener = { item: MenuItem ->
        when(item.itemId) {
            R.id.add_tags_button -> {
                fragment.newTagsDialog.show(
                    fragment.requireActivity().supportFragmentManager,
                    AddEditTagBottomSheetFragment.TAG
                )
                true
            }
            else -> false
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