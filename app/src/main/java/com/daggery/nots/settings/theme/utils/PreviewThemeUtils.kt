package com.daggery.nots.settings.theme.utils

import android.content.Context
import com.daggery.nots.MainActivity
import com.daggery.nots.R
import com.daggery.nots.databinding.ListItemNoteBinding
import com.daggery.nots.settings.theme.view.PreviewThemeFragment
import com.daggery.nots.settings.theme.view.PreviewThemeFragmentArgs
import com.daggery.nots.utils.ThemeEnum
import com.google.android.material.color.MaterialColors
import java.text.SimpleDateFormat
import java.util.*

fun ListItemNoteBinding.bind(fragmentUtils: PreviewThemeUtils, number: String) {
    noteTitle.text = "Preview $number"
    noteBody.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod "
        .plus("tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis ")
        .plus("nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.")
    noteDate.text = fragmentUtils.getCurrentDate()
    listItemLayout.setBackgroundResource(R.drawable.bg_note_item)
}

class PreviewThemeUtils(
    private val fragment: PreviewThemeFragment,
    private val args: PreviewThemeFragmentArgs
) {

    internal fun getCurrentDate(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        return formatter.format(date)
    }


    fun applyTheme() {
        with(fragment) {
            val themeKey = when(args.themeEnum) {
                ThemeEnum.DEFAULT_DARK -> R.style.DefaultDarkTheme
                ThemeEnum.NORD -> R.style.NordTheme
                ThemeEnum.AZALEA -> R.style.AzaleaTheme
            }
            (requireActivity() as MainActivity).updateTheme(themeKey)
        }
    }
}