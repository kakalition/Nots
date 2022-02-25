package com.daggery.nots.settings.theme.data

import android.view.View
import androidx.annotation.ColorRes

data class TileThemeData(
    val title: String,
    @ColorRes val surfaceColorRes: Int,
    @ColorRes val secondaryColorRes: Int,
    val onClickListener: ((View) -> Unit)?
)
