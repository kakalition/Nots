package com.daggery.nots.settings.theme.utils

import androidx.navigation.fragment.findNavController
import com.daggery.nots.MainActivity
import com.daggery.nots.R
import com.daggery.nots.databinding.TileFilledNoteItemBinding
import com.daggery.nots.settings.theme.view.PreviewThemeFragment
import com.daggery.nots.utils.ThemeEnum
import java.text.SimpleDateFormat
import java.util.*

fun TileFilledNoteItemBinding.bind(fragmentUtils: PreviewThemeUtils, number: String) {
    with(fragmentUtils) {
        noteTitle.text = getPreviewTitleStringRes(number)
        noteBody.text = getPreviewBodyStringRes()
        noteDate.text = getCurrentDate()
    }
    listItemLayout.setBackgroundResource(R.drawable.bg_note_item)
}

class PreviewThemeUtils(private val fragment: PreviewThemeFragment) {

    fun getPreviewTitleStringRes(number: String): String {
        return fragment.resources.getString(R.string.fragment_preview_theme_note_title, number)
    }

    fun getPreviewBodyStringRes(): String {
        return fragment.resources.getString(R.string.lorem_ipsum)
    }

    fun bindsToolbar() {
        fragment.viewBinding.previewBinding.toolbarBinding.apply {
            toolbarTitle.text = fragment.getString(R.string.fragment_preview_theme_preview_text)
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener {
                fragment.findNavController().navigateUp()
            }
        }
    }

    internal fun getCurrentDate(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        return formatter.format(date)
    }


    fun applyTheme() {
        with(fragment) {
            val themeKey = when(args.themeEnum) {
                ThemeEnum.MATERIAL_YOU -> R.style.MaterialYouTheme
                ThemeEnum.DARK_THEME -> R.style.DarkTheme
                ThemeEnum.NORD -> R.style.NordTheme
                ThemeEnum.STEEL_BLUE -> R.style.SteelBlueTheme
                ThemeEnum.ROYAL_LAVENDER -> R.style.RoyalLavenderTheme
                ThemeEnum.HEATHER_BERRY -> R.style.HeatherBerryTheme
            }
            (requireActivity() as MainActivity).updateTheme(themeKey)
        }
    }

}