package com.daggery.nots.settings.layout.utils

import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.daggery.nots.MainActivity
import com.daggery.nots.R
import com.daggery.nots.databinding.TileNoteItemBinding
import com.daggery.nots.settings.layout.view.PreviewHomeLayoutFragment
import com.google.android.material.color.MaterialColors
import java.text.SimpleDateFormat
import java.util.*

fun TileNoteItemBinding.bind(fragmentUtils: PreviewHomeLayoutUtils, number: String, backgroundRes: Int) {
    with(fragmentUtils) {
        if(backgroundRes == R.drawable.bg_note_item_outlined) {
            noteTitle.setTextColor(outlinedTextColor)
            noteBody.setTextColor(outlinedTextColor)
            noteDate.setTextColor(outlinedTextColor)
        }
        noteTitle.text = getPreviewTitleStringRes(number)
        noteBody.text = getPreviewBodyStringRes()
        noteDate.text = getCurrentDate()
    }
    listItemLayout.setBackgroundResource(backgroundRes)
}

class PreviewHomeLayoutUtils(private val fragment: PreviewHomeLayoutFragment) {

    val outlinedTextColor = MaterialColors.getColor(
            fragment.requireContext(),
            com.google.android.material.R.attr.colorOnSurface,
            fragment.resources.getColor(R.color.white, null)
    )

    private val navigationClickListener: (View) -> Unit = { _: View ->
        fragment.findNavController().navigateUp()
    }

    fun getPreviewTitleStringRes(number: String): String {
        return fragment.resources.getString(R.string.fragment_preview_theme_note_title, number)
    }

    fun getPreviewBodyStringRes(): String {
        return fragment.resources.getString(R.string.lorem_ipsum)
    }

    internal fun getCurrentDate(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        return formatter.format(date)
    }

    // TODO: Can be optimized
    fun applyLayout(layoutId: Int) {
        with(fragment) {
            (requireActivity() as MainActivity).viewModel.applyLayout(layoutId)
            findNavController().navigateUp()
        }
    }

    fun bindsToolbar() {
        fragment.viewBinding.toolbarBinding.apply {
            toolbarTitle.text = "Preview"
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener(navigationClickListener)
        }
    }
}