package com.daggery.nots.home.utils

import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.home.view.FilterFragment

class FilterFragmentUtils(private val fragment: FilterFragment) {

    internal fun bindsToolbar() {
        with(fragment.viewBinding.toolbarBinding.toolbar) {
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { fragment.findNavController().navigateUp() }
            title = "Tags Filter"
        }

    }
}