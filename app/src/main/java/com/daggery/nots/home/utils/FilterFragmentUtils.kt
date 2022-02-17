package com.daggery.nots.home.utils

import android.app.Activity
import android.graphics.Color
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.home.view.FilterFragment
import com.daggery.nots.home.view.NewTagsDialogFragment
import com.google.android.material.color.MaterialColors

class FilterFragmentUtils(private val fragment: FilterFragment) {

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
            title = "Tag Filter"
        }
    }

    private val menuItemClickListener = { item: MenuItem ->
        when(item.itemId) {
            R.id.add_tags_button -> {
                fragment.newTagsDialog.show(
                    fragment.requireActivity().supportFragmentManager,
                    NewTagsDialogFragment.TAG
                )
                true
            }
            else -> false
        }
    }

}