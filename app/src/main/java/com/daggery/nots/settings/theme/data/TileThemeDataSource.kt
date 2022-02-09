package com.daggery.nots.settings.theme.data

import com.daggery.nots.R
import com.daggery.nots.settings.theme.utils.TileThemeData

class TileThemeDataSource {

    companion object {
        val defaultDarkTile = TileThemeData(
            title = "Default Dark",
            primaryColorRes = R.color.default_dark_primary,
            secondaryColorRes = R.color.default_dark_secondary,
            surfaceColorRes = R.color.default_dark_surface,
            themePortraitRes = R.drawable.default_black_portrait,
            onClickListener = null
        )

        val nordTile = TileThemeData(
            title = "Nord",
            primaryColorRes = R.color.nord_primary,
            secondaryColorRes = R.color.nord_secondary,
            surfaceColorRes = R.color.nord_surface,
            themePortraitRes = R.drawable.nord_portrait,
            onClickListener = null
        )

        val azaleaTile = TileThemeData(
            title = "Azalea",
            primaryColorRes = R.color.azalea_primary,
            secondaryColorRes = R.color.azalea_secondary,
            surfaceColorRes = R.color.azalea_surface,
            themePortraitRes = R.drawable.azalea_portrait,
            onClickListener = null
        )
    }

}